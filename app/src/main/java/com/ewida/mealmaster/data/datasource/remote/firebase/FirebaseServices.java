package com.ewida.mealmaster.data.datasource.remote.firebase;

import com.ewida.mealmaster.data.datasource.remote.firebase.auth.FirebaseAuthContract;
import com.ewida.mealmaster.data.datasource.remote.firebase.database.FirebaseDatabaseContract;

public interface FirebaseServices extends FirebaseAuthContract, FirebaseDatabaseContract {
}
