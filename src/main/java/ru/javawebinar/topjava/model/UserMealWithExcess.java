package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final CaloriesPerDay caloriesPerDay;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, CaloriesPerDay caloriesPerDay) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.caloriesPerDay = caloriesPerDay;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + this.caloriesPerDay.getExcess() +
                '}';
    }
}
