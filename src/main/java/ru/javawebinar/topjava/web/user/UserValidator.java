package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.AbstractValidator;
import ru.javawebinar.topjava.web.SecurityUtil;

@Component
public class UserValidator extends AbstractValidator {

    @Autowired
    private UserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz) || User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Integer userId = null;
        String email = null;
        if (UserTo.class.equals(target.getClass())) {
            UserTo userTo = (UserTo) target;
            email = userTo.getEmail();
            userId = userTo.getId();
        } else {
            User user = (User) target;
            email = user.getEmail();
            userId = user.getId();
        }
        if (userId == null) {
            AuthorizedUser authorizedUser = SecurityUtil.safeGet();
            if (authorizedUser != null) {
                userId = SecurityUtil.authUserId();
            }
        }
        if (email != null && repository.getByEmail(email) != null) {
            String emailCurrent = "";
            if (userId != null) {
                emailCurrent = repository.get(userId).getEmail();
            }
            if (!emailCurrent.equals(email)) {
                rejectValue(errors, "email", "validator.error.user.email_duplicate");
            }
        }
    }
}
