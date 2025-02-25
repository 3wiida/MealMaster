package com.ewida.mealmaster.main.plans.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.datasource.local.MealsLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.local.UserLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.UserRemoteDataSourceImpl;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.Plan;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepositoryImpl;
import com.ewida.mealmaster.data.repository.user_repo.UserRepositoryImpl;
import com.ewida.mealmaster.databinding.FragmentPlansBinding;
import com.ewida.mealmaster.main.plans.PlannedMealsContracts;
import com.ewida.mealmaster.main.plans.presenter.PlannedMealsPresenter;
import com.ewida.mealmaster.meal_details.view.MealDetailsActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kizitonwose.calendar.core.Week;
import com.kizitonwose.calendar.core.WeekDay;
import com.kizitonwose.calendar.view.WeekDayBinder;
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class PlansFragment extends Fragment implements PlannedMealsContracts.View, DayViewContainer.OnDayClickListener, PlannedMealsAdapter.OnMealClickListener {

    private FragmentPlansBinding binding;
    private PlannedMealsAdapter adapter;
    private PlannedMealsContracts.presenter presenter;
    private LocalDate selectedDate;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlansBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedDate = LocalDate.now();
        presenter = new PlannedMealsPresenter(
                this,
                MealsRepositoryImpl.getInstance(MealsRemoteDataSourceImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(requireActivity())),
                UserRepositoryImpl.getInstance(UserRemoteDataSourceImpl.getInstance(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance()), UserLocalDataSourceImpl.getInstance(requireActivity()))
        );
        String date = selectedDate.getDayOfMonth() + "-" + selectedDate.getMonthValue() + "-" + selectedDate.getYear();
        presenter.getPlannedMeals(date);
        initViews();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        setupCalendar();
        adapter = new PlannedMealsAdapter(this);
        binding.rvPlanedMeals.setAdapter(adapter);
        int day = selectedDate.getDayOfMonth();
        String month = selectedDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        binding.tvMealsLabel.setText(day + "th of " + month + " Meals");
    }

    private void setupCalendar() {
        binding.calendarView.setDayBinder(new WeekDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view, PlansFragment.this);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void bind(@NonNull DayViewContainer container, WeekDay weekDay) {
                container.getBinding().calendarDayText.setText(weekDay.getDate().getDayOfMonth() + "");
                container.setDay(weekDay);
                if (weekDay.getDate().isEqual(selectedDate)) {
                    container.getBinding().calendarDayText.setTextColor(requireActivity().getColor(R.color.onPrimaryColor));
                    container.getBinding().calendarDayText.setBackgroundResource(R.drawable.selected_day_bg);
                } else {
                    container.getBinding().calendarDayText.setTextColor(requireActivity().getColor(R.color.black));
                    container.getBinding().calendarDayText.setBackground(null);
                }
            }
        });

        binding.calendarView.setWeekHeaderBinder(new WeekHeaderFooterBinder<CalenderHeaderViewContainer>() {
            @NonNull
            @Override
            public CalenderHeaderViewContainer create(@NonNull View view) {
                return new CalenderHeaderViewContainer(view);
            }

            @Override
            public void bind(@NonNull CalenderHeaderViewContainer container, Week week) {
                String month = week.getDays().get(0).getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
                container.getTvMonth().setText(month);
            }
        });

        LocalDate currentDate = LocalDate.now();
        YearMonth currentMonth = YearMonth.now();
        LocalDate startDate = currentMonth.minusMonths(1).atEndOfMonth();
        LocalDate endDate = currentMonth.plusMonths(1).atEndOfMonth();
        binding.calendarView.setup(startDate, endDate, DayOfWeek.SATURDAY);
        binding.calendarView.scrollToWeek(currentDate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDayClicked(WeekDay day) {
        selectedDate = day.getDate();
        binding.calendarView.notifyCalendarChanged();
        int dayOfMonth = day.getDate().getDayOfMonth();
        String month = day.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        binding.tvMealsLabel.setText(dayOfMonth + "th of " + month + " Meals");
        String date = day.getDate().getDayOfMonth() + "-" + day.getDate().getMonthValue() + "-" + day.getDate().getYear();
        presenter.getPlannedMeals(date);
    }

    @Override
    public void showPlannedMeals(List<Meal> meals) {
        meals.forEach(meal-> Log.d("```TAG```", "showPlannedMeals: "+meal.getStrMeal()));
        adapter.submitList(meals);
        binding.emptyState.setVisibility(meals.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMessage(String msg) {
        Snackbar.make(binding.getRoot(),msg,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRemovedSnackBar(Plan plan) {
        Snackbar snackbar = Snackbar
                .make(binding.getRoot(), "Meal removed from plan successfully", Snackbar.LENGTH_LONG)
                .setAction("Undo", view -> {
                    presenter.planMeal(plan);
                });
        snackbar.show();
    }

    @Override
    public void onMealClicked(Meal meal) {
        Intent mealDetailsIntent = new Intent(requireActivity(), MealDetailsActivity.class);
        mealDetailsIntent.putExtra(MealDetailsActivity.MEAL_OBJECT_EXTRA, (Parcelable) meal);
        startActivity(mealDetailsIntent);
    }

    @Override
    public void onRemoveClicked(Meal meal) {
        String date = selectedDate.getDayOfMonth() + "-" + selectedDate.getMonthValue() + "-" + selectedDate.getYear();
        presenter.unPlanMeal(meal,date);
    }
}