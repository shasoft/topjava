package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;
import ru.javawebinar.topjava.model.GroupMealsPerDay;

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
        // Список отфильтрованных элементов
        List<UserMealWithExcess> userMealsWithExcess = new ArrayList<>();
        // Группировка по дням
        Map<LocalDate, GroupMealsPerDay> groupsMealsPerDay = new HashMap<>();
        //
        meals.forEach(meal -> {
            // Получим объект группировки еды за день
            GroupMealsPerDay groupMealsPerDay = groupsMealsPerDay.computeIfAbsent(
                    meal.getDateTime().toLocalDate(),
                    day -> new GroupMealsPerDay(limitCaloriesPerDay)
            );
            // Добавим текущую еду
            groupMealsPerDay.add(meal);
            // Проверим попадание элемента в границы временного интервала
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                // Добавим еду в результирующий список
                userMealsWithExcess.add(
                        new UserMealWithExcess(
                                meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                groupMealsPerDay)
                );
            }
        });
        return userMealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay
    ) {
        return meals.stream()
                // Группируем по дате
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate()))
                .values()
                .stream()
                // Создаем для каждого дня свою группу со списком еды
                .map(mealsPerDay -> new GroupMealsPerDay(caloriesPerDay, mealsPerDay))
                // Каждую группу по дням превратим в единый список
                .flatMap(groupMealsPerDay ->
                        groupMealsPerDay.geMeals()
                                .stream()
                                // Отфильтруем единый список по условию
                                .filter(meal ->
                                        TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)
                                )
                                .map(meal -> new UserMealWithExcess(
                                                meal.getDateTime(),
                                                meal.getDescription(),
                                                meal.getCalories(),
                                                groupMealsPerDay
                                        )
                                )
                )
                // Преобразуем поток в список
                .collect(Collectors.toList());
    }
}
