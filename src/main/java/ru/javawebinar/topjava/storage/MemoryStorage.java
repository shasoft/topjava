package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryStorage implements Storage {
    protected Map<Integer, Meal> storage = new ConcurrentHashMap<>();
    static ReentrantLock locker = new ReentrantLock(); // создаем заглушку
    static protected Integer nextId=1;
    public MemoryStorage() {
    }
    static protected Integer getNextId() {
        locker.lock();
        Integer ret = nextId;
        nextId = nextId + 1;
        locker.unlock();
        return ret;
    }
    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Meal get(Integer id) {
        return storage.get(id);
    }

    @Override
    public Meal set(Meal meal) {
        storage.put(meal.getId(),meal);
        return meal;
    }

    @Override
    public Meal add(Meal meal) {
        Integer id = getNextId();
        Meal mealAdd = new Meal(id,meal.getDateTime(),meal.getDescription(),meal.getCalories());
        storage.putIfAbsent(id,mealAdd);
        return mealAdd;
    }

    @Override
    public void delete(Meal meal) {
        storage.remove(meal.getId());
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

}
