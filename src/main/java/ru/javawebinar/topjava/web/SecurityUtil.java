package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static Integer currentUserId = 1;

    public static Integer authUserId() {
        return currentUserId;
    }

    public static void setAuthUserId(Integer userId) {
        currentUserId = userId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}