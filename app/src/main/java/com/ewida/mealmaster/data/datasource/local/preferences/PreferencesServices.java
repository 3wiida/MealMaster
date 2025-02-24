package com.ewida.mealmaster.data.datasource.local.preferences;

public interface PreferencesServices {
    String getCurrentUserName();
    void setCurrentUserName(String name);
    String getCurrentUserId();
    void setCurrentUserId(String id);
    Boolean isFirstTime();
    void setFirstTime(boolean isFirstTime);
    Boolean isAfterAuth();
    void setAfterAuth(boolean isAfterAuth);
}
