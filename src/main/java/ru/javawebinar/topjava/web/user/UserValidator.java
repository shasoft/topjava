package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

@Component
public class UserValidator implements Validator {

    @Autowired
    private MessageSource messageSource;

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
            userId = SecurityUtil.authUserId();
        }
        if (email != null && repository.getByEmail(email) != null) {
            final String emailCurrent = repository.get(userId).getEmail();
            if (!emailCurrent.equals(email)) {
                rejectValue(errors, "email", "validator.error.user.email_duplicate");
            }
        }
    }

    protected void rejectValue(Errors errors, String field, String errorMessageI18n) {
        errors.rejectValue(field, errorMessageI18n, messageSource.getMessage(errorMessageI18n, null, LocaleContextHolder.getLocale()));
    }
}
