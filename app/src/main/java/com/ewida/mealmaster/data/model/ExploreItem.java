package com.ewida.mealmaster.data.model;

public class ExploreItem {
    private final String thumbnail;
    private final String title;

    public ExploreItem(String thumbnail, String title) {
        this.thumbnail = thumbnail;
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }
}
