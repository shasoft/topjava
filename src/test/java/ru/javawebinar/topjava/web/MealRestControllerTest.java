package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.json.JsonUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.web.meal.MealRestController.REST_URL;

class MealRestControllerTest extends AbstractControllerTest {
    @Autowired
    private MealService mealService;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(getTos(meals, DEFAULT_CALORIES_PER_DAY)));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(urlForId(MEAL1_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal meal = MealTestData.getNew();
        Meal mealFromRequest = MEAL_MATCHER.readFromJson(
                perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(meal)))
                        .andExpect(status().isCreated())
        );
        int newId = mealFromRequest.id();
        meal.setId(newId);
        MEAL_MATCHER.assertMatch(mealFromRequest, meal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), meal);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(urlForId(MEAL1_ID + 1)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID + 1, USER_ID));
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(urlForId(MEAL1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void getBetween() throws Exception {
        final List<MealTo> mealsTo = List.of(
                new MealTo(meal7.getId(), meal7.getDateTime(), meal7.getDescription(), meal7.getCalories(), true),
                new MealTo(meal6.getId(), meal6.getDateTime(), meal6.getDescription(), meal6.getCalories(), true)
        );
        final String date = "2020-01-31";
        final String startTime = "13:00:00";
        final String endTime = "23:59:59";
        perform(MockMvcRequestBuilders.get(REST_URL + "/filter")
                .param("startDate", date)
                .param("startTime", startTime)
                .param("endDate", date)
                .param("endTime", endTime)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(mealsTo));
    }

    private String urlForId(int id) {
        return REST_URL + "/" + id;
    }
}