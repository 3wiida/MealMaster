package com.ewida.mealmaster.model.data_sources.firebase.database;

import com.ewida.mealmaster.model.pojo.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDB implements FirebaseDatabaseContract {
    private static final String USER_DB_PATH = "Users";

    @Override
    public Task<Void> insertUser(User user) {
         return FirebaseDatabase.getInstance().getReference().child(USER_DB_PATH).child(user.getId()).setValue(user);
    }

    @Override
    public DatabaseReference getUserByID(String id){
        return FirebaseDatabase.getInstance().getReference(USER_DB_PATH).child(id);
    }

}
