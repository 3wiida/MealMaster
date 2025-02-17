package com.ewida.mealmaster.auth.login;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.databinding.FragmentLoginBinding;
import com.ewida.mealmaster.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;


public class LoginFragment extends Fragment implements LoginViewContract {

    //TODO handle loading in google auth button when there is no connection

    private FragmentLoginBinding binding;
    private LoginPresenterContract presenter;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenter(this);
        googleSignInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                presenter.handleGoogleAuthResult(getContext(), task);
            } else {
                binding.btnGoogleLogin.setLoading(false);
                binding.btnLogin.setLoading(false);
                binding.btnSkip.setClickable(true);
                binding.btnSignup.setClickable(true);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        binding.setPresenter((LoginPresenter) presenter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClicks();
    }

    private void initClicks() {
        binding.btnLogin.setOnClickListener(view -> loginUser());
        binding.btnGoogleLogin.setOnClickListener(view -> startGoogleAuth());
        binding.btnSignup.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.actionToRegisterFragment));
        binding.btnSkip.setOnClickListener(view -> navigateToHomeScreen());
    }

    private void loginUser() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        presenter.handleLoginClick(getContext(), email, password);
    }

    private void startGoogleAuth() {
        binding.btnGoogleLogin.setLoading(true);
        binding.btnLogin.setClickable(false);
        binding.btnSkip.setClickable(false);
        binding.btnSignup.setClickable(false);
        googleSignInLauncher.launch(presenter.getGoogleSignInIntent(getContext()));
    }

    @Override
    public void showLoaderOnLoginButton() {
        binding.btnLogin.setLoading(true);
        binding.btnGoogleLogin.setClickable(false);
        binding.btnSkip.setClickable(false);
        binding.btnSignup.setClickable(false);
    }

    @Override
    public void navigateToHomeScreen() {
        binding.btnLogin.setLoading(false);
        binding.btnGoogleLogin.setLoading(false);
        if (getActivity() != null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void showErrorMessage(String msg) {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_LONG).show();
        binding.btnLogin.setLoading(false);
        binding.btnGoogleLogin.setLoading(true);
        binding.btnSkip.setClickable(true);
        binding.btnSignup.setClickable(true);
    }

}