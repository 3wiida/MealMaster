package com.ewida.mealmaster.main.plans.view;

import android.view.View;
import androidx.annotation.NonNull;
import com.ewida.mealmaster.databinding.CalendarDayLayoutBinding;
import com.kizitonwose.calendar.core.WeekDay;
import com.kizitonwose.calendar.view.ViewContainer;

public class DayViewContainer extends ViewContainer {
    private final CalendarDayLayoutBinding binding;
    private final OnDayClickListener onDayClickListener;
    private WeekDay day;

    public DayViewContainer(@NonNull View view, OnDayClickListener onDayClickListener) {
        super(view);
        this.binding = CalendarDayLayoutBinding.bind(view);
        this.onDayClickListener = onDayClickListener;
        initClicks();
    }

    private void initClicks() {
        binding.calendarDayText.setOnClickListener(view -> {
            onDayClickListener.onDayClicked(day);
        });
    }

    public CalendarDayLayoutBinding getBinding() {
        return binding;
    }

    public void setDay(WeekDay day) {
        this.day = day;
    }

    public interface OnDayClickListener {
        void onDayClicked(WeekDay weekDay);
    }
}
