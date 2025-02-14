package com.ewida.mealmaster.app_components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ewida.mealmaster.R;

public class AppButton extends ConstraintLayout {

    private ConstraintLayout btnContainer;
    private TextView btnLabel;
    private ProgressBar btnProgressBar;
    private ImageView btnIcon;
    private int disabledBackgroundResource;
    private int enabledBackgroundResource;

    private int icon;

    public AppButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attributeSet) {
        inflate(this.getContext(), R.layout.app_button_layout, this);
        TypedArray attrs = getContext().obtainStyledAttributes(attributeSet, R.styleable.AppButton);
        String text = attrs.getString(R.styleable.AppButton_btnText);
        icon = attrs.getResourceId(R.styleable.AppButton_btnIcon, -1);
        enabledBackgroundResource = attrs.getResourceId(R.styleable.AppButton_btnBackgroundRes, R.drawable.app_btn_background);
        disabledBackgroundResource = attrs.getResourceId(R.styleable.AppButton_btnDisabledBackgroundRes, R.drawable.app_btn_background);
        int textColor = attrs.getColor(R.styleable.AppButton_btnTextColor, getResources().getColor(R.color.onPrimaryColor));
        boolean isLoading = attrs.getBoolean(R.styleable.AppButton_isBtnLoading, false);

        btnContainer = findViewById(R.id.clContainer);
        btnLabel = findViewById(R.id.tvTitle);
        btnIcon = findViewById(R.id.ivIcon);
        btnProgressBar = findViewById(R.id.progressBar);

        btnLabel.setText(text);
        btnLabel.setTextColor(textColor);
        btnContainer.setBackgroundResource(enabledBackgroundResource);
        btnProgressBar.setVisibility(isLoading ? VISIBLE : GONE);
        btnIcon.setVisibility((icon != -1) ? VISIBLE : GONE);
        if (icon != -1) {
            btnIcon.setImageResource(icon);
        }
        attrs.recycle();
    }

    public void setLoading(boolean isLoading) {
        btnProgressBar.setVisibility(isLoading ? VISIBLE : INVISIBLE);
        btnLabel.setVisibility(isLoading ? INVISIBLE : VISIBLE);
        if (icon != -1) {
            btnIcon.setVisibility(isLoading ? INVISIBLE : VISIBLE);
        }
        setBtnEnabled(!isLoading);
    }

    public void setBtnEnabled(boolean isEnabled) {
        setClickable(isEnabled);
        btnContainer.setBackgroundResource(isEnabled ? enabledBackgroundResource : disabledBackgroundResource);
    }

    public void setText(String text) {
        btnLabel.setText(text);
    }

}
