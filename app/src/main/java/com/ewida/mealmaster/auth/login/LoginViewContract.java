package com.ewida.mealmaster.auth.login;

public interface LoginViewContract {
    void navigateToHomeScreen();
    void showLoaderOnLoginButton();
    void showErrorMessage(String msg);
}
