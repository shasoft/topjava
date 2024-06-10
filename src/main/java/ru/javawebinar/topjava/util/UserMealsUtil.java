package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;
import ru.javawebinar.topjava.model.CaloriesPerDay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500), new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000), new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500), new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100), new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000), new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500), new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // Список отфильтрованных элементов
        List<UserMealWithExcess> oUserMealsWithExcess = new ArrayList<>();
        // Группировка по дням
        Map<LocalDate, CaloriesPerDay> mapCaloriesPerDay = new HashMap<>();
        //
        meals.forEach((UserMeal meal) -> {
            // Определим день
            LocalDate day = meal.getDateTime().toLocalDate();
            // Получим объект суммы калорий за день
            CaloriesPerDay oCaloriesPerDay;
            if (mapCaloriesPerDay.containsKey(day)) {
                oCaloriesPerDay = mapCaloriesPerDay.get(day);
            } else {
                oCaloriesPerDay = new CaloriesPerDay(caloriesPerDay);
                mapCaloriesPerDay.put(day, oCaloriesPerDay);
            }
            // Добавим калории за день
            oCaloriesPerDay.add(meal);
            // Проверим попадание элемента в границы временного интервала
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                oUserMealsWithExcess.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), oCaloriesPerDay));
            }
        });
        return oUserMealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(
                // Группируем по дате
                Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate())
        ).values().stream().map((List<UserMeal> listGroupByDate) -> {
            // Создаем для каждого дня свой объект со списком еды
            return new CaloriesPerDay(caloriesPerDay, listGroupByDate);
        }).flatMap((CaloriesPerDay oCaloriesPerDay) -> {
            // Теперь каждый список по дням превратим в единый список отфильтровав по временному периоду
            return oCaloriesPerDay.geMeals().stream()
                    .filter((UserMeal meal) ->
                            TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)
                    )
                    .map((UserMeal meal) -> new UserMealWithExcess(
                                    meal.getDateTime(),
                                    meal.getDescription(),
                                    meal.getCalories(),
                                    oCaloriesPerDay
                            )
                    );
        }).collect(Collectors.toList());
    }
}
