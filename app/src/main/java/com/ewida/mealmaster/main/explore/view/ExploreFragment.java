package com.ewida.mealmaster.main.explore.view;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.ewida.mealmaster.data.datasource.local.MealsLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.model.ExploreItem;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepositoryImpl;
import com.ewida.mealmaster.databinding.FragmentExploreBinding;
import com.ewida.mealmaster.explore_meals.ExploreMealsContracts;
import com.ewida.mealmaster.explore_meals.view.ExploreMeals;
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ExplorePresenter(
                this,
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(requireContext())
                )
        );
        presenter.getAllCategories();
        presenter.getAllAreas();
        presenter.getAllIngredients();
        initViews();
        initClicks();
        initSearchObservable();
    }

    private void initViews() {
        binding.searchChipGroup.check(binding.searchChipGroup.getChildAt(0).getId());

        searchResultsAdapter = new SearchResultsAdapter();
        binding.rvSearchResults.setAdapter(searchResultsAdapter);

        categoriesAdapter = new ExploreItemAdapter();
        binding.rvCategories.setAdapter(categoriesAdapter);

        areasAdapter = new ExploreItemAdapter();
        binding.rvAreas.setAdapter(areasAdapter);

        ingredientsAdapter = new ExploreItemAdapter();
        binding.rvIngredients.setAdapter(ingredientsAdapter);
    }

    private void initClicks() {
        binding.searchChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            String searchQuery = binding.etSearch.getEditableText().toString();
            if (!searchQuery.isEmpty())
                presenter.search(searchQuery, getExploreType());
        });

        categoriesAdapter.setOnItemClick(item -> {
            Intent exploreMealsIntent = new Intent(requireActivity(), ExploreMeals.class);
            exploreMealsIntent.putExtra(ExploreMeals.EXPLORE_TYPE_EXTRA, ExploreMealsContracts.ExploreType.CATEGORY);
            exploreMealsIntent.putExtra(ExploreMeals.EXPLORE_QUERY_EXTRA, item.getTitle());
            startActivity(exploreMealsIntent);
        });

        areasAdapter.setOnItemClick(item -> {
            Intent exploreMealsIntent = new Intent(requireActivity(), ExploreMeals.class);
            exploreMealsIntent.putExtra(ExploreMeals.EXPLORE_TYPE_EXTRA, ExploreMealsContracts.ExploreType.AREA);
            exploreMealsIntent.putExtra(ExploreMeals.EXPLORE_QUERY_EXTRA, item.getTitle());
            startActivity(exploreMealsIntent);
        });

        ingredientsAdapter.setOnItemClick(item -> {
            Intent exploreMealsIntent = new Intent(requireActivity(), ExploreMeals.class);
            exploreMealsIntent.putExtra(ExploreMeals.EXPLORE_TYPE_EXTRA, ExploreMealsContracts.ExploreType.INGREDIENT);
            exploreMealsIntent.putExtra(ExploreMeals.EXPLORE_QUERY_EXTRA, item.getTitle());
            startActivity(exploreMealsIntent);
        });

        searchResultsAdapter.setOnItemClick(item -> {
            Intent exploreMealsIntent = new Intent(requireActivity(), ExploreMeals.class);
            exploreMealsIntent.putExtra(ExploreMeals.EXPLORE_TYPE_EXTRA, getExploreType());
            exploreMealsIntent.putExtra(ExploreMeals.EXPLORE_QUERY_EXTRA, item.getTitle());
            startActivity(exploreMealsIntent);
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
            presenter.search(query, getExploreType());
        });
    }

    private ExploreMealsContracts.ExploreType getExploreType() {
        ExploreMealsContracts.ExploreType type;
        if (binding.searchChipGroup.getCheckedChipId() == R.id.chipCategory) {
            type = ExploreMealsContracts.ExploreType.CATEGORY;
        } else if (binding.searchChipGroup.getCheckedChipId() == R.id.chipArea) {
            type = ExploreMealsContracts.ExploreType.AREA;
        } else {
            type = ExploreMealsContracts.ExploreType.INGREDIENT;
        }
        return type;
    }

    @Override
    public void hideCategoriesLoadingState() {
        if (isAdded() && getActivity() != null) {
            binding.rvIngredients.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_anim));
        }
    }

    @Override
    public void hideAreasLoadingState() {
        if (isAdded() && getActivity() != null) {
            binding.rvAreas.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_anim));
        }
    }

    @Override
    public void hideIngredientsLoadingState() {
        if (isAdded() && getActivity() != null) {
            binding.rvIngredients.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_anim));
        }
    }

    @Override
    public void showAllCategories(List<ExploreItem> categories) {
        categoriesAdapter.setList(categories);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.rvCategories.setVisibility(View.VISIBLE);
            binding.categoryShimmer.setVisibility(View.INVISIBLE);
            if (isAdded() && getActivity() != null) {
                binding.rvCategories.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in_anim));
            }
        }, 300L);
    }

    @Override
    public void showAllAreas(List<ExploreItem> areas) {
        areasAdapter.setList(areas);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.rvAreas.setVisibility(View.VISIBLE);
            binding.areaShimmer.setVisibility(View.INVISIBLE);
            if (isAdded() && getActivity() != null) {
                binding.rvAreas.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in_anim));
            }
        }, 300L);
    }

    @Override
    public void showAllIngredients(List<ExploreItem> ingredients) {
        ingredientsAdapter.setList(ingredients);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.rvIngredients.setVisibility(View.VISIBLE);
            binding.ingredientShimmer.setVisibility(View.INVISIBLE);
            if (isAdded() && getActivity() != null) {
                binding.rvIngredients.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in_anim));
            }
        }, 300L);
    }

    @Override
    public void showSectionGroup() {
        binding.sectionsGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSectionGroup() {
        binding.sectionsGroup.setVisibility(View.GONE);
    }

    @Override
    public void showSearchResults(List<ExploreItem> results) {
        searchResultsAdapter.setList(results);
        binding.rvSearchResults.setAdapter(searchResultsAdapter);
        binding.rvSearchResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSearchResults() {
        searchResultsAdapter.setList(new ArrayList<>());
    }

    @Override
    public void showErrorMessage(String errorMsg) {
        Snackbar.make(binding.getRoot(), errorMsg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        binding.etSearch.setText("");
        binding.searchChipGroup.check(binding.searchChipGroup.getChildAt(0).getId());
        super.onDestroyView();
    }
}