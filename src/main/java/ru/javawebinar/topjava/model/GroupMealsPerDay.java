package ru.javawebinar.topjava.model;

import java.util.ArrayList;
import java.util.List;

public class GroupMealsPerDay {
    private final int limit;
    private int value;
    private final List<UserMeal> meals;

    public GroupMealsPerDay(int limit) {
        this.limit = limit;
        this.value = 0;
        this.meals = new ArrayList<>();
    }

    public GroupMealsPerDay(int limit, List<UserMeal> meals) {
        this.limit = limit;
        this.value = meals.stream().mapToInt(UserMeal::getCalories).sum();
        this.meals = meals;
    }

    public void add(UserMeal meal) {
        this.value += meal.getCalories();
        this.meals.add(meal);
    }

    public boolean getExcess() {
        return this.value > this.limit;
    }

    public List<UserMeal> geMeals() {
        return this.meals;
    }

    @Override
    public String toString() {
        return "CaloriesPerDay{" + value + "}";
    }
}
