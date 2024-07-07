package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealStorageMemory implements MealStorage {
    protected static final AtomicInteger atomicId = new AtomicInteger(0);
    protected Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public Meal create(Meal meal) {
        Meal mealAdd = new Meal(atomicId.incrementAndGet(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        storage.put(mealAdd.getId(), mealAdd);
        return mealAdd;
    }

    @Override
    public Meal read(int id) {
        return storage.get(id);
    }

    @Override
    public Meal update(Meal meal) {
        if (storage.containsKey(meal.getId())) {
            storage.put(meal.getId(), meal);
            return meal;
        }
        return null;
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
