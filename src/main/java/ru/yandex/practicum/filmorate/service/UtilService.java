package ru.yandex.practicum.filmorate.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilService {
    public static final LocalDate MIN_FILM_DATE = LocalDate.of(1895, 12, 28);
    private static final DateTimeFormatter DATE_WITH_TIME = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
    private static final DateTimeFormatter ONLY_DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String getDateWithTimeStr(LocalDateTime date) {
        return DATE_WITH_TIME.format(date);
    }

    public static String getOnlyDateStr(LocalDate date) {
        return ONLY_DATE.format(date);
    }
}
