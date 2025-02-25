package com.ewida.mealmaster.main.plans.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ewida.mealmaster.R;
import com.kizitonwose.calendar.view.ViewContainer;

public class CalenderHeaderViewContainer extends ViewContainer {

    private final TextView tvMonth;
    public CalenderHeaderViewContainer(@NonNull View view) {
        super(view);
        tvMonth=view.findViewById(R.id.tvMonth);
    }

    public TextView getTvMonth() {
        return tvMonth;
    }
}
