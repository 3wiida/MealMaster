package com.ewida.mealmaster.model.data_sources.firebase.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseAuthentication implements FirebaseAuthContract{

    @Override
    public Task<AuthResult> signUpWithEmailAndPassword(String email, String password) {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> authWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        return FirebaseAuth.getInstance().signInWithCredential(credential);
    }
}
