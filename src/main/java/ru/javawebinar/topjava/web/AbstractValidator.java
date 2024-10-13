package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class AbstractValidator implements Validator {

    @Autowired
    private MessageSource messageSource;

    protected void rejectValue(Errors errors, String field, String errorMessageI18n) {
        errors.rejectValue(field, errorMessageI18n, messageSource.getMessage(errorMessageI18n, null, LocaleContextHolder.getLocale()));
    }
}
