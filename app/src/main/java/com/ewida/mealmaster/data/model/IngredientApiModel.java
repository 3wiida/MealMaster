package com.ewida.mealmaster.data.model;

public class IngredientApiModel {
    private String strDescription;
    private String strIngredient;
    private String idIngredient;
    private String strType;

    public String getStrDescription() {
        return strDescription;
    }

    public String getStrIngredient() {
        return strIngredient;
    }

    public String getidIngredient() {
        return idIngredient;
    }

    public String getStrType() {
        return strType;
    }

    public ExploreItem toExploreItem(){
        String thumbnail = "https://www.themealdb.com/images/ingredients/" + strIngredient + "-Medium.png";
        return new ExploreItem(thumbnail,strIngredient);
    }
}
