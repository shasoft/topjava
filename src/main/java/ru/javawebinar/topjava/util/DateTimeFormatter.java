package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

public class DateTimeFormatter {

    public static class LocalDateConverter implements Formatter<LocalDate> {

        @Override
        public LocalDate parse(@Nullable String text, Locale locale) {
            return DateTimeUtil.parseLocalDate(text);
        }

        @Override
        public String print(LocalDate ld, Locale locale) {
            return ld.format(ISO_LOCAL_DATE);
        }
    }

    public static class LocalTimeConverter implements Formatter<LocalTime> {

        @Override
        public LocalTime parse(@Nullable String text, Locale locale) {
            return DateTimeUtil.parseLocalTime(text);
        }

        @Override
        public String print(LocalTime lt, Locale locale) {
            return lt.format(ISO_LOCAL_TIME);
        }
    }
}

