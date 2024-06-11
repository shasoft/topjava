package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int limitCaloriesPerDay
    ) {
        // Суммируем калории по каждому дню
        Map<LocalDate, Integer> sumCaloriesPerDay = new HashMap<>();
        meals.forEach(meal -> {
            LocalDate day = meal.getDateTime().toLocalDate();
            sumCaloriesPerDay.put(
                    day,
                    sumCaloriesPerDay.getOrDefault(day, 0) + meal.getCalories()
            );
        });
        // Список отфильтрованных элементов для возврата из метода
        List<UserMealWithExcess> userMealsWithExcess = new ArrayList<>();
        meals.forEach((UserMeal meal) -> {
            // Проверим попадание элемента в границы временного интервала
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                // Определим: в течение дня было превышение лимита калорий?
                boolean excess = sumCaloriesPerDay.get(
                        meal.getDateTime().toLocalDate()
                ) > limitCaloriesPerDay;
                // Добавить в итоговый список
                userMealsWithExcess.add(
                        new UserMealWithExcess(
                                meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                excess)
                );
            }
        });
        return userMealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int limitCaloriesPerDay
    ) {
        // Суммируем калории по каждому дню
        Map<LocalDate, Integer> sumCaloriesPerDay = meals.stream()
                .collect(
                        Collectors.groupingBy(
                                // Группируем по дню
                                meal -> meal.getDateTime().toLocalDate(),
                                // Суммируем калории за день
                                Collectors.summingInt(UserMeal::getCalories)
                        )
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        sumCaloriesPerDay.getOrDefault(meal.getDateTime().toLocalDate(), 0) > limitCaloriesPerDay
                ))
                .collect(Collectors.toList());
    }
}
