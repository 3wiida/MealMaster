package com.ewida.mealmaster.main.explore;

import com.ewida.mealmaster.data.model.Category;
import com.ewida.mealmaster.data.model.ExploreItem;

import java.util.List;

public interface ExploreContracts {
    interface Presenter{
        void getAllCategories();
        void getAllAreas();
        void getAllIngredients();
    }

    interface View{
        void showAllCategories(List<ExploreItem> categories);
        void showAllAreas(List<ExploreItem> areas);
        void showAllIngredients(List<ExploreItem> ingredients);
        void showErrorMessage(String errorMsg);
    }
}
