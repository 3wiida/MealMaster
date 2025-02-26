package com.ewida.mealmaster.data.model;

public class Profile {
    private final String username;
    private final String email;
    private final UserStatistics userStatistics;

    public Profile(String username, String email, UserStatistics userStatistics) {
        this.username = username;
        this.email = email;
        this.userStatistics = userStatistics;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UserStatistics getUserStatistics() {
        return userStatistics;
    }
}
