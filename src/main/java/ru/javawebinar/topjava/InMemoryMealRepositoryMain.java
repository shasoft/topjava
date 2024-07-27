package ru.javawebinar.topjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

public class InMemoryMealRepositoryMain {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryMain.class);

    public static void main(String[] args) {
        InMemoryMealRepository repository = new InMemoryMealRepository();
        log.info("All");
        List<Meal> all = repository.select(SecurityUtil.authUserId(), null, null);
        for (Meal meal : all) {
            log.info("\tmeal {}", meal);
        }
        LocalDate startDate = LocalDate.of(2020, 1, 31);
        log.info(">={}", startDate);
        List<Meal> startMeals = repository.select(SecurityUtil.authUserId(), startDate, null);
        for (Meal meal : startMeals) {
            log.info("\tmeal {}", meal);
        }
        LocalDate endDate = LocalDate.of(2020, 1, 30);
        log.info("<={}", endDate);
        List<Meal> endMeals = repository.select(SecurityUtil.authUserId(), null, endDate);
        for (Meal meal : endMeals) {
            log.info("\tmeal {}", meal);
        }
    }
}
