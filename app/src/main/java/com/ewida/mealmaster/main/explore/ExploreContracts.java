package com.ewida.mealmaster.main.explore;

import com.ewida.mealmaster.data.model.Category;
import com.ewida.mealmaster.data.model.ExploreItem;
import com.ewida.mealmaster.explore_meals.ExploreMealsContracts;
import com.ewida.mealmaster.main.explore.presenter.ExplorePresenter;

import java.util.List;

public interface ExploreContracts {
    interface Presenter{
        void getAllCategories();
        void getAllAreas();
        void getAllIngredients();
        void search(String query, ExploreMealsContracts.ExploreType type);
    }

    interface View{
        void hideCategoriesLoadingState();
        void hideAreasLoadingState();
        void hideIngredientsLoadingState();
        void showSectionGroup();
        void hideSectionGroup();
        void showSearchResults(List<ExploreItem> results);
        void hideSearchResults();
        void showAllCategories(List<ExploreItem> categories);
        void showAllAreas(List<ExploreItem> areas);
        void showAllIngredients(List<ExploreItem> ingredients);
        void showErrorMessage(String errorMsg);
    }
    enum SearchType {
        CATEGORY,
        AREA,
        INGREDIENT
    }
}
