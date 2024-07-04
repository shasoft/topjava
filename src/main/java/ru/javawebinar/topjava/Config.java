package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MemoryStorage;
import ru.javawebinar.topjava.storage.Storage;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Config {
    private static final Config INSTANCE = new Config();

    private final DateTimeFormatter datetimeFormat ;

    private final Storage storage;

    public final Integer caloriesPerDay = 2000;

    private Config() {
        datetimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        storage = new MemoryStorage();
        //
        storage.add(new Meal( LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        storage.add(new Meal( LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        storage.add(new Meal( LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        storage.add(new Meal( LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        storage.add(new Meal( LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        storage.add(new Meal( LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        storage.add(new Meal( LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }
    public DateTimeFormatter getDateTimeFormatter()
    {
        return datetimeFormat;
    }

    public static Config get() {
        return INSTANCE;
    }

    public Storage getStorage() {
        return storage;
    }
}
