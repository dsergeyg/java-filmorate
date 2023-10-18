package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;


@Data
public class Film {
    private static int idSequence = 0;
    public static final LocalDate MINDATE = LocalDate.of(1895, 12, 28);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private int id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private long longDuration;
    private Duration duration;

    public Film(Integer id, @NonNull String name, String description, @NonNull LocalDate releaseDate, long longDuration) throws ValidationException {
        this.id = Objects.requireNonNullElseGet(id, () -> ++idSequence);
        this.name = name;
        this.description = description;
        if (releaseDate.isAfter(MINDATE.minusDays(1)))
            this.releaseDate = releaseDate;
        else
            throw new ValidationException("дата релиза — не раньше " + simpleDateFormat.format(Date.valueOf(MINDATE)));
        this.longDuration = longDuration;
        this.duration = Duration.ofMinutes(longDuration);
    }
}
