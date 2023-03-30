package com.example.MyBookShopApp.services.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class DateTimeFormatter {

    private static final List<String> dateFormats = Arrays.asList("dd-MM-yyyy", "dd.MM.yyyy");

    public static LocalDateTime fromDateTime(String date) {
        LocalDate localDate = dateFormatter(date);
        return localDate != null ? localDate.atStartOfDay() : LocalDateTime.now().minus(1, ChronoUnit.CENTURIES);
    }

    public static LocalDateTime toDateTime(String date) {
        LocalDate localDate = dateFormatter(date);
        return localDate != null ? localDate.atTime(23, 59, 59) : LocalDateTime.now();
    }

    private static LocalDate dateFormatter(String date) {
        for (String format : dateFormats) {
            try {
                return LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern(format));
            } catch (DateTimeParseException ignored) {
            }
        }
        Logger.getLogger(DateTimeFormatter.class.getName()).info("Unknown date format: " + date);
        return null;
    }
}
