package com.ewida.mealmaster.main.profile.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.databinding.LogoutDialogLayoutBinding;

public class LogoutDialog extends Dialog {

    private final OnLogoutConfirmedListener onLogoutConfirmedListener;
    private LogoutDialogLayoutBinding binding;

    public LogoutDialog(@NonNull Context context, OnLogoutConfirmedListener onLogoutConfirmedListener) {
        super(context);
        this.onLogoutConfirmedListener = onLogoutConfirmedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LogoutDialogLayoutBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(R.drawable.logout_dialog_bg);
        }
        setCancelable(true);
        initClicks();
    }

    private void initClicks() {
        binding.logoutBtn.setOnClickListener(view -> {
            onLogoutConfirmedListener.onLogoutConfirmed();
        });

        binding.cancelBtn.setOnClickListener(view->{
            dismiss();
        });
    }

    public interface OnLogoutConfirmedListener {
        void onLogoutConfirmed();
    }
}
