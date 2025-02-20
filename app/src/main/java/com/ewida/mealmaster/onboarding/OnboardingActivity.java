package com.ewida.mealmaster.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.auth.AuthActivity;
import com.ewida.mealmaster.databinding.ActivityOnboardingBinding;
import com.ewida.mealmaster.data.model.OnboardingItem;
import com.ewida.mealmaster.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private int currentPage = 0;

    private ActivityOnboardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        initClicks();
        initObservers();
    }

    private void initViews() {
        OnboardingPagerAdapter pagerAdapter = new OnboardingPagerAdapter(getOnboardingItems());
        binding.vpItems.setAdapter(pagerAdapter);
        binding.dotsIndicator.attachTo(binding.vpItems);
    }

    private void initClicks() {
        binding.btnNext.setOnClickListener(view -> {
            if (currentPage < 2) {
                binding.vpItems.setCurrentItem(++currentPage, true);
            } else {
                updateFirstOpenInSharedPref();
                navigateToAuthActivity();
            }
        });
    }

    private void initObservers() {
        binding.vpItems.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position == 2) {
                    binding.btnNext.setText(getString(R.string.get_started));
                } else {
                    binding.btnNext.setText(getString(R.string.next));
                }
                currentPage = position;
            }
        });
    }

    private void updateFirstOpenInSharedPref(){
        getSharedPreferences(Constants.SharedPref.PREFS_FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(Constants.SharedPref.IS_FIRST_OPEN_KEY,false)
                .apply();
    }

    private void navigateToAuthActivity() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    private List<OnboardingItem> getOnboardingItems() {
        List<OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingItem(R.drawable.onboarding_image_1, getString(R.string.onboarding_one_title), getString(R.string.onboarding_one_body)));
        items.add(new OnboardingItem(R.drawable.onboarding_image_2, getString(R.string.onboarding_two_title), getString(R.string.onboarding_two_body)));
        items.add(new OnboardingItem(R.drawable.onboarding_image_3, getString(R.string.onboarding_three_title), getString(R.string.onboarding_three_body)));
        return items;
    }

}