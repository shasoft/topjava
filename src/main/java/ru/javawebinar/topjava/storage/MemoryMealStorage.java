package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealStorage implements MealStorage {
    private final AtomicInteger generatorId = new AtomicInteger(0);
    final private Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public Meal create(Meal meal) {
        Meal mealAdd = new Meal(generatorId.incrementAndGet(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        storage.put(mealAdd.getId(), mealAdd);
        return mealAdd;
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public void update(Meal meal) {
        storage.replace(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

}
