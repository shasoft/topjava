package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    public static Integer authUserId() {
        return 1;
    }

    public static int authUserIdOrFail() {
        final Integer userId = authUserId();
        if (userId == null) {
            throw new NotFoundException("User is not authorized");
        }
        return userId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}