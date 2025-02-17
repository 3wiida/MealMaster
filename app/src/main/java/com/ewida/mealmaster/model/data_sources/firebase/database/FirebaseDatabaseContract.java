package com.ewida.mealmaster.model.data_sources.firebase.database;

import com.ewida.mealmaster.model.pojo.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public interface FirebaseDatabaseContract {
    Task<Void> insertUser(User user);

    DatabaseReference getUserByID(String id);
}
