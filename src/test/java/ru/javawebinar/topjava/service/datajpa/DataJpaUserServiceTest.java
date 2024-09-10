package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Test
    public void getWithMeals() throws Exception {
        User user = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
        MEAL_MATCHER.assertMatch(user.getMeals(), meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void getWithNotMeals() throws Exception {
        User guest = service.getWithMeals(GUEST_ID);
        USER_MATCHER.assertMatch(guest, UserTestData.guest);
        Assert.assertEquals(0, user.getMeals().size());
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }
}
