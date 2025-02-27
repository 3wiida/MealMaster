package com.ewida.mealmaster.data.datasource.remote.firebase.database;

import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.Plan;
import com.ewida.mealmaster.data.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public interface FirebaseDatabaseContract {
    Task<Void> insertUser(User user);

    DatabaseReference getUserByID(String id);

    void updateUserData(List<Meal> meals, List<Plan> plans);

}
