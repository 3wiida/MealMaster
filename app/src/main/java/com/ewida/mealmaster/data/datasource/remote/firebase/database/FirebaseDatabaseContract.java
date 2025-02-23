package com.ewida.mealmaster.data.datasource.remote.firebase.database;

import com.ewida.mealmaster.data.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public interface FirebaseDatabaseContract {
    Task<Void> insertUser(User user);
    DatabaseReference getUserByID(String id);
}
