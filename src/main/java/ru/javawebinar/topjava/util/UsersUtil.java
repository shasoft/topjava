package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {
    public static final List<User> users = Arrays.asList(
            new User(1, "Admin", "valera@mydomen.ru", "qwerty"),
            new User(2, "Валера", "valera@mydomen.ru", "123")
    );
}
