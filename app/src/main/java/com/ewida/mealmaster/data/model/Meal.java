package com.ewida.mealmaster.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Meal implements Parcelable {

    private String strIngredient10;

    private String strIngredient12;

    private String strIngredient11;

    private String strIngredient14;

    private String strCategory;

    private String strIngredient13;

    private String strIngredient16;

    private String strIngredient15;

    private String strIngredient18;

    private String strIngredient17;

    private String strArea;

    private String strIngredient19;

    private String strTags;

    private String idMeal;

    private String strInstructions;

    private String strIngredient1;

    private String strIngredient3;

    private String strIngredient2;

    private String strIngredient20;

    private String strIngredient5;

    private String strIngredient4;

    private String strIngredient7;

    private String strIngredient6;

    private String strIngredient9;

    private String strIngredient8;

    private String strMealThumb;

    private String strMeasure20;

    private String strYoutube;

    private String strMeal;

    private String strMeasure12;

    private String strMeasure13;

    private String strMeasure10;

    private String strMeasure11;

    private String strSource;

    private String strMeasure9;

    private String strMeasure7;

    private String strMeasure8;

    private String strMeasure5;

    private String strMeasure6;

    private String strMeasure3;

    private String strMeasure4;

    private String strMeasure1;

    private String strMeasure18;

    private String strMeasure2;

    private String strMeasure19;

    private String strMeasure16;

    private String strMeasure17;

    private String strMeasure14;

    private String strMeasure15;

    protected Meal(Parcel in) {
        strIngredient10 = in.readString();
        strIngredient12 = in.readString();
        strIngredient11 = in.readString();
        strIngredient14 = in.readString();
        strCategory = in.readString();
        strIngredient13 = in.readString();
        strIngredient16 = in.readString();
        strIngredient15 = in.readString();
        strIngredient18 = in.readString();
        strIngredient17 = in.readString();
        strArea = in.readString();
        strIngredient19 = in.readString();
        strTags = in.readString();
        idMeal = in.readString();
        strInstructions = in.readString();
        strIngredient1 = in.readString();
        strIngredient3 = in.readString();
        strIngredient2 = in.readString();
        strIngredient20 = in.readString();
        strIngredient5 = in.readString();
        strIngredient4 = in.readString();
        strIngredient7 = in.readString();
        strIngredient6 = in.readString();
        strIngredient9 = in.readString();
        strIngredient8 = in.readString();
        strMealThumb = in.readString();
        strMeasure20 = in.readString();
        strYoutube = in.readString();
        strMeal = in.readString();
        strMeasure12 = in.readString();
        strMeasure13 = in.readString();
        strMeasure10 = in.readString();
        strMeasure11 = in.readString();
        strSource = in.readString();
        strMeasure9 = in.readString();
        strMeasure7 = in.readString();
        strMeasure8 = in.readString();
        strMeasure5 = in.readString();
        strMeasure6 = in.readString();
        strMeasure3 = in.readString();
        strMeasure4 = in.readString();
        strMeasure1 = in.readString();
        strMeasure18 = in.readString();
        strMeasure2 = in.readString();
        strMeasure19 = in.readString();
        strMeasure16 = in.readString();
        strMeasure17 = in.readString();
        strMeasure14 = in.readString();
        strMeasure15 = in.readString();
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public String getStrIngredient10() {
        return this.strIngredient10;
    }

    public String getStrIngredient12() {
        return this.strIngredient12;
    }

    public String getStrIngredient11() {
        return this.strIngredient11;
    }

    public String getStrIngredient14() {
        return this.strIngredient14;
    }

    public String getStrCategory() {
        return this.strCategory;
    }

    public String getStrIngredient13() {
        return this.strIngredient13;
    }

    public String getStrIngredient16() {
        return this.strIngredient16;
    }

    public String getStrIngredient15() {
        return this.strIngredient15;
    }

    public String getStrIngredient18() {
        return this.strIngredient18;
    }

    public String getStrIngredient17() {
        return this.strIngredient17;
    }

    public String getStrArea() {
        return this.strArea;
    }

    public String getStrIngredient19() {
        return this.strIngredient19;
    }

    public String getStrTags() {
        if (strTags == null || strTags.trim().equals(" ")) {
            strTags = "N/A";
        } else {
            List<String> tags = Arrays.asList(strTags.split(","));
            StringBuilder refactoredTags = new StringBuilder();
            for (int i = 0; i < tags.size(); i++) {
                refactoredTags.append(tags.get(i));
                if (i != tags.size() - 1) {
                    refactoredTags.append(", ");
                }
            }
            strTags = refactoredTags.toString();
        }
        return strTags;
    }

    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            try {
                String ingredient = (String) Meal.class.getDeclaredMethod("getStrIngredient" + i).invoke(this);
                String measure = (String) Meal.class.getDeclaredMethod("getStrMeasure" + i).invoke(this);

                if (ingredient != null && !ingredient.trim().isEmpty()) {
                    String thumbnailUrl = "https://www.themealdb.com/images/ingredients/" + ingredient + "-Small.png";
                    ingredients.add(new Ingredient(thumbnailUrl, ingredient, (measure != null && !measure.trim().isEmpty()) ? measure : ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ingredients;
    }

    public String getIdMeal() {
        return this.idMeal;
    }

    public String getStrInstructions() {
        return this.strInstructions;
    }

    public String getStrIngredient1() {
        return this.strIngredient1;
    }

    public String getStrIngredient3() {
        return this.strIngredient3;
    }

    public String getStrIngredient2() {
        return this.strIngredient2;
    }

    public String getStrIngredient20() {
        return this.strIngredient20;
    }

    public String getStrIngredient5() {
        return this.strIngredient5;
    }

    public String getStrIngredient4() {
        return this.strIngredient4;
    }

    public String getStrIngredient7() {
        return this.strIngredient7;
    }

    public String getStrIngredient6() {
        return this.strIngredient6;
    }

    public String getStrIngredient9() {
        return this.strIngredient9;
    }

    public String getStrIngredient8() {
        return this.strIngredient8;
    }

    public String getStrMealThumb() {
        return this.strMealThumb;
    }

    public String getStrMeasure20() {
        return this.strMeasure20;
    }

    public String getStrYoutube() {
        if(this.strYoutube!=null){
            return this.strYoutube.split("v=")[1];
        }
        return null;
    }

    public String getStrMeal() {
        return this.strMeal;
    }

    public String getStrMeasure12() {
        return this.strMeasure12;
    }

    public String getStrMeasure13() {
        return this.strMeasure13;
    }

    public String getStrMeasure10() {
        return this.strMeasure10;
    }

    public String getStrMeasure11() {
        return this.strMeasure11;
    }

    public String getStrSource() {
        return this.strSource;
    }

    public String getStrMeasure9() {
        return this.strMeasure9;
    }

    public String getStrMeasure7() {
        return this.strMeasure7;
    }

    public String getStrMeasure8() {
        return this.strMeasure8;
    }

    public String getStrMeasure5() {
        return this.strMeasure5;
    }

    public String getStrMeasure6() {
        return this.strMeasure6;
    }

    public String getStrMeasure3() {
        return this.strMeasure3;
    }

    public String getStrMeasure4() {
        return this.strMeasure4;
    }

    public String getStrMeasure1() {
        return this.strMeasure1;
    }

    public String getStrMeasure18() {
        return this.strMeasure18;
    }

    public String getStrMeasure2() {
        return this.strMeasure2;
    }

    public String getStrMeasure19() {
        return this.strMeasure19;
    }

    public String getStrMeasure16() {
        return this.strMeasure16;
    }

    public String getStrMeasure17() {
        return this.strMeasure17;
    }

    public String getStrMeasure14() {
        return this.strMeasure14;
    }

    public String getStrMeasure15() {
        return this.strMeasure15;
    }

    public void setStrTags(String strTags) {
        this.strTags = strTags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(strIngredient10);
        dest.writeString(strIngredient12);
        dest.writeString(strIngredient11);
        dest.writeString(strIngredient14);
        dest.writeString(strCategory);
        dest.writeString(strIngredient13);
        dest.writeString(strIngredient16);
        dest.writeString(strIngredient15);
        dest.writeString(strIngredient18);
        dest.writeString(strIngredient17);
        dest.writeString(strArea);
        dest.writeString(strIngredient19);
        dest.writeString(strTags);
        dest.writeString(idMeal);
        dest.writeString(strInstructions);
        dest.writeString(strIngredient1);
        dest.writeString(strIngredient3);
        dest.writeString(strIngredient2);
        dest.writeString(strIngredient20);
        dest.writeString(strIngredient5);
        dest.writeString(strIngredient4);
        dest.writeString(strIngredient7);
        dest.writeString(strIngredient6);
        dest.writeString(strIngredient9);
        dest.writeString(strIngredient8);
        dest.writeString(strMealThumb);
        dest.writeString(strMeasure20);
        dest.writeString(strYoutube);
        dest.writeString(strMeal);
        dest.writeString(strMeasure12);
        dest.writeString(strMeasure13);
        dest.writeString(strMeasure10);
        dest.writeString(strMeasure11);
        dest.writeString(strSource);
        dest.writeString(strMeasure9);
        dest.writeString(strMeasure7);
        dest.writeString(strMeasure8);
        dest.writeString(strMeasure5);
        dest.writeString(strMeasure6);
        dest.writeString(strMeasure3);
        dest.writeString(strMeasure4);
        dest.writeString(strMeasure1);
        dest.writeString(strMeasure18);
        dest.writeString(strMeasure2);
        dest.writeString(strMeasure19);
        dest.writeString(strMeasure16);
        dest.writeString(strMeasure17);
        dest.writeString(strMeasure14);
        dest.writeString(strMeasure15);
    }
}

