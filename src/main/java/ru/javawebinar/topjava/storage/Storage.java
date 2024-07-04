package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {

    void clear();

    Meal set(Meal meal);

    Meal add(Meal meal);

    Meal get(Integer id);

    void delete(Meal meal);

    List<Meal> getAll();
}
