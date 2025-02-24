package com.ewida.mealmaster.main.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.auth.AuthActivity;
import com.ewida.mealmaster.databinding.FragmentProfileBinding;
import com.ewida.mealmaster.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogout.setOnClickListener(mView->{
            FirebaseAuth.getInstance().signOut();
            requireActivity().startActivity(new Intent(requireActivity(), AuthActivity.class));
            //requireContext().getSharedPreferences(Constants.SharedPref.PREFS_FILE_NAME, Context.MODE_PRIVATE).edit().putBoolean(Constants.SharedPref.IS_AFTER_AUTH_KEY, true).apply();
            requireActivity().finish();
        });
    }
}