package com.ewida.mealmaster.main.explore.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.model.ExploreItem;
import com.ewida.mealmaster.data.repository.MealsRepositoryImpl;
import com.ewida.mealmaster.databinding.FragmentExploreBinding;
import com.ewida.mealmaster.main.explore.ExploreContracts;
import com.ewida.mealmaster.main.explore.presenter.ExplorePresenter;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class ExploreFragment extends Fragment implements ExploreContracts.View {

    private FragmentExploreBinding binding;
    private ExploreContracts.Presenter presenter;
    private ExploreItemAdapter categoriesAdapter;
    private ExploreItemAdapter areasAdapter;
    private ExploreItemAdapter ingredientsAdapter;
    private SearchResultsAdapter searchResultsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        searchResultsAdapter = new SearchResultsAdapter();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ExplorePresenter(this, MealsRepositoryImpl.getInstance(MealsRemoteDataSourceImpl.getInstance()));
        presenter.getAllCategories();
        presenter.getAllAreas();
        presenter.getAllIngredients();
        initViews();
        initClicks();
        initSearchObservable();
    }

    private void initViews() {
        binding.searchChipGroup.check(binding.searchChipGroup.getChildAt(0).getId());
    }

    private void initClicks() {
        binding.searchChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            ExploreContracts.SearchType searchType;
            String searchQuery = binding.etSearch.getEditableText().toString();
            if (binding.searchChipGroup.getCheckedChipId() == R.id.chipCategory) {
                searchType = ExploreContracts.SearchType.CATEGORY;
            } else if (binding.searchChipGroup.getCheckedChipId() == R.id.chipArea) {
                searchType = ExploreContracts.SearchType.AREA;
            } else {
                searchType = ExploreContracts.SearchType.INGREDIENT;
            }
            if (!searchQuery.isEmpty())
                presenter.search(searchQuery, searchType);
        });

        categoriesAdapter.setOnItemClick(item -> {

        });

        areasAdapter.setOnItemClick(item -> {

        });

        ingredientsAdapter.setOnItemClick(item -> {

        });
    }

    @SuppressLint("CheckResult")
    private void initSearchObservable() {
        Observable<String> searchObservable = Observable.create(emitter -> {
            binding.etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence query, int start, int before, int count) {
                    emitter.onNext(query.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        });

        searchObservable.debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(query -> {
            ExploreContracts.SearchType searchType;
            if (binding.searchChipGroup.getCheckedChipId() == R.id.chipCategory) {
                searchType = ExploreContracts.SearchType.CATEGORY;
            } else if (binding.searchChipGroup.getCheckedChipId() == R.id.chipArea) {
                searchType = ExploreContracts.SearchType.AREA;
            } else {
                searchType = ExploreContracts.SearchType.INGREDIENT;
            }
            presenter.search(query, searchType);
        });
    }

    @Override
    public void hideCategoriesLoadingState() {
        binding.rvIngredients.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim));

    }

    @Override
    public void hideAreasLoadingState() {
        binding.rvAreas.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim));

    }

    @Override
    public void hideIngredientsLoadingState() {
        binding.rvIngredients.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim));

    }

    @Override
    public void showAllCategories(List<ExploreItem> categories) {
        categoriesAdapter = new ExploreItemAdapter(categories);
        binding.rvCategories.setAdapter(categoriesAdapter);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.rvCategories.setVisibility(View.VISIBLE);
            binding.categoryShimmer.setVisibility(View.INVISIBLE);
            binding.rvCategories.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_anim));
        }, 300L);
    }

    @Override
    public void showAllAreas(List<ExploreItem> areas) {
        areasAdapter = new ExploreItemAdapter(areas);
        binding.rvAreas.setAdapter(areasAdapter);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.rvAreas.setVisibility(View.VISIBLE);
            binding.areaShimmer.setVisibility(View.INVISIBLE);
            binding.rvAreas.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_anim));
        }, 300L);
    }

    @Override
    public void showAllIngredients(List<ExploreItem> ingredients) {
        ingredientsAdapter = new ExploreItemAdapter(ingredients);
        binding.rvIngredients.setAdapter(ingredientsAdapter);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.rvIngredients.setVisibility(View.VISIBLE);
            binding.ingredientShimmer.setVisibility(View.INVISIBLE);
            binding.rvIngredients.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_anim));
        }, 300L);
    }

    @Override
    public void showSectionGroup() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.sectionsGroup.setVisibility(View.VISIBLE);
            binding.sectionsGroup.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_anim));
        }, 300L);
    }

    @Override
    public void hideSectionGroup() {
        binding.sectionsGroup.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim));
    }

    @Override
    public void showSearchResults(List<ExploreItem> results) {
        searchResultsAdapter.setList(results);
        binding.rvSearchResults.setAdapter(searchResultsAdapter);
        binding.rvSearchResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSearchResults() {
        binding.rvSearchResults.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim));
        searchResultsAdapter.setList(new ArrayList<>());
    }

    @Override
    public void showErrorMessage(String errorMsg) {
        Snackbar.make(binding.getRoot(), errorMsg, Snackbar.LENGTH_LONG).show();
    }

}