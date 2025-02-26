package com.ewida.mealmaster.data.datasource.remote;

import com.ewida.mealmaster.data.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRemoteDataSourceImpl implements UserRemoteDataSource {

    private final FirebaseAuth auth;
    private final FirebaseDatabase db;
    public static final String USER_DB_PATH = "Users";
    private static UserRemoteDataSource instance;

    private UserRemoteDataSourceImpl(FirebaseAuth auth, FirebaseDatabase db) {
        this.auth = auth;
        this.db = db;
    }

    public static UserRemoteDataSource getInstance(FirebaseAuth auth, FirebaseDatabase db){
        if(instance == null){
            instance = new UserRemoteDataSourceImpl(auth, db);
        }
        return instance;
    }

    @Override
    public Task<AuthResult> signUpWithEmailAndPassword(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> authWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        return auth.signInWithCredential(credential);
    }

    @Override
    public Task<Void> insertUser(User user) {
        return db.getReference().child(USER_DB_PATH).child(user.getId()).setValue(user);
    }

    @Override
    public DatabaseReference getUserByID(String id) {
        return db.getReference(USER_DB_PATH).child(id);
    }

    @Override
    public String getCurrentUserEmail() {
        if(auth.getCurrentUser()!= null){
            return auth.getCurrentUser().getEmail();
        }
        return "-";
    }
}
