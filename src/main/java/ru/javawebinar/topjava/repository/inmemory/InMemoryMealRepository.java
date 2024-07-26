package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        for (Meal meal : MealsUtil.meals) {
            this.save(meal, 1);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} for user {}", meal, userId);
        final Map<Integer, Meal> repository = this.getRepositoryForUser(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} for user {}", id, userId);
        final Map<Integer, Meal> repository = this.getRepositoryForUser(userId);
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} for user {}", id, userId);
        final Map<Integer, Meal> repository = this.getRepositoryForUser(userId);
        return repository.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for user {}", userId);
        final Map<Integer, Meal> repository = this.getRepositoryForUser(userId);
        final ArrayList<Meal> all = new ArrayList<>(repository.values());
        all.sort(Comparator.comparing(Meal::getDateTime).reversed());
        return all;
    }

    @Override
    public List<Meal> select(int userId, LocalDate startDate, LocalDate endDate) {
        Stream<Meal> streamMeals = this.getAll(userId).stream();
        if (startDate != null) {
            streamMeals.filter(meal -> meal.getDateTime().toLocalDate().isAfter(startDate)).collect(Collectors.toList());
        }
        if (endDate != null) {
            streamMeals.filter(meal -> meal.getDateTime().toLocalDate().isBefore(endDate)).collect(Collectors.toList());
        }
        return streamMeals.collect(Collectors.toList());
    }

    private Map<Integer, Meal> getRepositoryForUser(int userId) {
        return repository.computeIfAbsent(userId, key -> new ConcurrentHashMap<>());
    }
}

