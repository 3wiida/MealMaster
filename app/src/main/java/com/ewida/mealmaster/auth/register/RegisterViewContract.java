package com.ewida.mealmaster.auth.register;

public interface RegisterViewContract {
    void navigateToHomeScreen();
    void showLoaderOnCreateAccountButton();
    void showErrorMessage(String msg);
}
