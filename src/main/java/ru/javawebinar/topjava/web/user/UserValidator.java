package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
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
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo userTo = (UserTo) target;
        final String email = userTo.getEmail();
        if (email != null && repository.getByEmail(email) != null) {
            final int userId = userTo.getId() != null ? userTo.id() : SecurityUtil.authUserId();
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
