package com.ewida.mealmaster.search.presenter;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.repository.MealsRepository;
import com.ewida.mealmaster.search.SearchContracts;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter implements SearchContracts.Presenter {

    private final SearchContracts.View view;
    private final MealsRepository repo;

    public SearchPresenter(SearchContracts.View view, MealsRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @SuppressLint("CheckResult")
    @Override
    public void search(String query) {
        view.showLoadingState();
        view.hideEmptyState();
        view.hideSearchResults();
        repo.search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> {
                            view.hideLoadingState();
                            if (mealResponse.getMeals() != null) {
                                view.showSearchResults(mealResponse.getMeals());
                            } else {
                                view.showEmptyState();
                            }
                        },
                        error -> view.showErrorMessage(error.getMessage())
                );
    }
}
