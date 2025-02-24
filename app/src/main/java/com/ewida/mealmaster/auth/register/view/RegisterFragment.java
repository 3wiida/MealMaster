package com.ewida.mealmaster.auth.register.view;

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
import com.ewida.mealmaster.auth.register.RegisterContracts;
import com.ewida.mealmaster.auth.register.presenter.RegisterPresenter;
import com.ewida.mealmaster.data.datasource.local.UserLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.UserRemoteDataSourceImpl;
import com.ewida.mealmaster.data.repository.user_repo.UserRepositoryImpl;
import com.ewida.mealmaster.databinding.FragmentRegisterBinding;
import com.ewida.mealmaster.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment implements RegisterContracts.View {

    private FragmentRegisterBinding binding;
    private RegisterContracts.Presenter presenter;
    private ActivityResultLauncher<Intent> googleAuthLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RegisterPresenter(
                this,
                UserRepositoryImpl.getInstance(
                        UserRemoteDataSourceImpl.getInstance(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance()),
                        UserLocalDataSourceImpl.getInstance(requireContext())
                )
        );

        googleAuthLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                presenter.handleGoogleAuthResult(task);
            } else {
                binding.btnGoogleRegister.setLoading(false);
                binding.btnCreateAccount.setLoading(false);
                binding.btnLogin.setClickable(true);
                binding.btnSkip.setClickable(true);
                binding.ivBack.setClickable(true);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        binding.setPresenter((RegisterPresenter) presenter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClicks();
    }

    private void initClicks() {
        binding.btnCreateAccount.setOnClickListener(view -> createAccount());
        binding.btnGoogleRegister.setOnClickListener(view -> startGoogleAuth());
        binding.btnLogin.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.actionToLoginFragment));
        binding.ivBack.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.actionToLoginFragment));
        binding.btnSkip.setOnClickListener(view -> navigateToHomeScreen());
    }

    private void createAccount() {
        String fullName = binding.etFullName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        presenter.handleCreateAccountClick(fullName, email, password);
    }

    private void startGoogleAuth() {
        binding.btnGoogleRegister.setLoading(true);
        binding.btnCreateAccount.setClickable(false);
        binding.btnLogin.setClickable(false);
        binding.btnSkip.setClickable(false);
        binding.ivBack.setClickable(false);
        googleAuthLauncher.launch(presenter.getGoogleAuthIntent(getContext()));
    }

    @Override
    public void showLoaderOnCreateAccountButton() {
        binding.btnCreateAccount.setLoading(true);
        binding.btnGoogleRegister.setClickable(false);
        binding.btnLogin.setClickable(false);
        binding.btnSkip.setClickable(false);
        binding.ivBack.setClickable(false);

    }

    @Override
    public void navigateToHomeScreen() {
        binding.btnCreateAccount.setLoading(false);
        binding.btnGoogleRegister.setLoading(false);
        Intent mainActivityIntent = new Intent(requireActivity(), MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        requireActivity().startActivity(mainActivityIntent);
        requireActivity().finish();
    }

    @Override
    public void showErrorMessage(String msg) {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_LONG).show();
        binding.btnCreateAccount.setLoading(false);
        binding.btnGoogleRegister.setLoading(false);
        binding.btnLogin.setClickable(true);
        binding.btnSkip.setClickable(true);
        binding.ivBack.setClickable(true);
    }

}