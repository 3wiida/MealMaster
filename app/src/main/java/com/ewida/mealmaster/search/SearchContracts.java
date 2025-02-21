package com.ewida.mealmaster.search;

import com.ewida.mealmaster.data.model.Meal;

import java.util.List;

public interface SearchContracts {
    interface Presenter{
        void search(String query);
    }

    interface View{
        void showSearchResults(List<Meal> results);
        void hideSearchResults();
        void showErrorMessage(String errorMsg);
        void showEmptyState();
        void hideEmptyState();
        void showLoadingState();
        void hideLoadingState();
    }
}
