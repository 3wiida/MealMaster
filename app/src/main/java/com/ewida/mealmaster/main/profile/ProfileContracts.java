package com.ewida.mealmaster.main.profile;

import com.ewida.mealmaster.data.model.Profile;

public interface ProfileContracts {
    interface Presenter{
        void getUserProfile();
    }
    interface View{
        void showUserData(Profile userProfile);
        void showMessage(String Msg);
    }
}
