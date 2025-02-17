package com.ewida.mealmaster.model.data_sources.firebase.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface FirebaseAuthContract {

    Task<AuthResult> signUpWithEmailAndPassword(String email, String password);

    Task<AuthResult> signInWithEmailAndPassword(String email, String password);

    Task<AuthResult> authWithGoogle(GoogleSignInAccount account);
}
