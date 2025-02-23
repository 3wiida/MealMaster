package com.ewida.mealmaster.search.view;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.datasource.local.MealsLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepositoryImpl;
import com.ewida.mealmaster.databinding.ActivitySearchBinding;
import com.ewida.mealmaster.meal_details.view.MealDetailsActivity;
import com.ewida.mealmaster.search.SearchContracts;
import com.ewida.mealmaster.search.presenter.SearchPresenter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class SearchActivity extends AppCompatActivity implements SearchContracts.View, SearchAdapter.OnMealClickListener {

    private ActivitySearchBinding binding;
    private SearchAdapter searchAdapter;
    private SearchPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        presenter = new SearchPresenter(
                this,
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(this)
                )
        );
        initViews();
        initQueryObservable();
    }

    private void initViews() {
        searchAdapter = new SearchAdapter(new ArrayList<>(), this);
        binding.rvSearchResults.setAdapter(searchAdapter);
    }

    @SuppressLint("CheckResult")
    private void initQueryObservable() {
        Observable<String> queryObservable = Observable.create(
                emitter -> binding.etSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence query, int start, int before, int count) {
                        hideEmptyQueryState();
                        emitter.onNext(query.toString().trim());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }));

        queryObservable
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> presenter.search(query));
    }

    private void hideEmptyQueryState() {
        binding.emptyQueryState.setVisibility(View.GONE);
    }

    @Override
    public void showSearchResults(List<Meal> results) {
        binding.rvSearchResults.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_anim));
        searchAdapter.setList(results);
    }

    @Override
    public void hideSearchResults() {
        searchAdapter.setList(new ArrayList<>());
        binding.rvSearchResults.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_anim));

    }

    @Override
    public void showEmptyState() {
        binding.emptyState.setVisibility(VISIBLE);
        binding.emptyState.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_anim));

    }

    @Override
    public void hideEmptyState() {
        binding.emptyState.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_anim));

    }

    @Override
    public void showLoadingState() {
        binding.progressBar.setVisibility(VISIBLE);
        binding.progressBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_anim));
    }

    @Override
    public void hideLoadingState() {
        binding.progressBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_anim));

    }

    @Override
    public void showErrorMessage(String errorMsg) {
        Snackbar.make(binding.getRoot(), errorMsg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onMealClicked(Meal meal) {
        Intent mealDetailsIntent = new Intent(this, MealDetailsActivity.class);
        mealDetailsIntent.putExtra(MealDetailsActivity.MEAL_OBJECT_EXTRA, (Parcelable) meal);
        startActivity(mealDetailsIntent);
    }
}