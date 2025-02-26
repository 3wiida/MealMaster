package com.ewida.mealmaster.data.model;

public class UserStatistics {
    private final int savedItemsCount;
    private final int plansCount;

    public UserStatistics(int savedItemsCount, int plansCount) {
        this.savedItemsCount = savedItemsCount;
        this.plansCount = plansCount;
    }

    public int getSavedItemsCount() {
        return savedItemsCount;
    }

    public int getPlansCount() {
        return plansCount;
    }
}
