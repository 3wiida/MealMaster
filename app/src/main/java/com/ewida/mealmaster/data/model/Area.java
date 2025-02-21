package com.ewida.mealmaster.data.model;

public class Area {
    private String strArea;

    public String getStrArea() {
        return strArea;
    }

    public ExploreItem toExploreItem() {
        return new ExploreItem(getAreaThumbnail(strArea), strArea);
    }

    private String getAreaThumbnail(String countryName) {
        String countryCode = getCountryCode(countryName);
        return "https://www.themealdb.com/images/icons/flags/big/64/" + countryCode + ".png";
    }

    private String getCountryCode(String countryName) {
        if (countryName == null) return "xx";
        countryName = countryName.toLowerCase();
        switch (countryName) {
            case "american":
                return "us";
            case "british":
                return "gb";
            case "canadian":
                return "ca";
            case "chinese":
                return "cn";
            case "croatian":
                return "hr";
            case "dutch":
                return "nl";
            case "egyptian":
                return "eg";
            case "french":
                return "fr";
            case "greek":
                return "gr";
            case "indian":
                return "in";
            case "irish":
                return "ie";
            case "italian":
                return "it";
            case "jamaican":
                return "jm";
            case "japanese":
                return "jp";
            case "kenyan":
                return "ke";
            case "malaysian":
                return "my";
            case "mexican":
                return "mx";
            case "moroccan":
                return "ma";
            case "polish":
                return "pl";
            case "portuguese":
                return "pt";
            case "russian":
                return "ru";
            case "spanish":
                return "es";
            case "thai":
                return "th";
            case "tunisian":
                return "tn";
            case "turkish":
                return "tr";
            case "vietnamese":
                return "vn";
            default:
                return "x";
        }
    }

}
