package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL_START_ID = START_SEQ + 3;
    public static final int NOT_FOUND = MEAL_START_ID + 100;

    public static final Meal meal1 = new Meal(MEAL_START_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Пользовательский Завтрак", 500);
    public static final Meal meal2 = new Meal(MEAL_START_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Пользовательский Обед", 1000);
    public static final Meal meal3 = new Meal(MEAL_START_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Пользовательский Ужин", 500);
    public static final Meal meal4 = new Meal(MEAL_START_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Пользовательский Еда на граничное значение", 100);
    public static final Meal meal5 = new Meal(MEAL_START_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Пользовательский Завтрак", 1000);
    public static final Meal meal6 = new Meal(MEAL_START_ID + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Пользовательский Обед", 500);
    public static final Meal meal7 = new Meal(MEAL_START_ID + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Пользовательский Ужин", 410);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2023, Month.MARCH, 3, 3, 3), "Перекус", 333);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setDescription("Ночная жрачка");
        updated.setDateTime(LocalDateTime.of(2022, Month.SEPTEMBER, 1, 4, 4));
        updated.setCalories(444);
        return updated;
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
