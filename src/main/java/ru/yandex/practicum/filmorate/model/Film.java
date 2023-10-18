package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;


@Validated
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

    private Duration duration;

    public Film(Integer id, @NonNull String name, String description, @NonNull LocalDate releaseDate, long duration)
            throws ValidationException {
        this.name = name;
        this.description = description;
        if (releaseDate.isAfter(MINDATE.minusDays(1)))
            this.releaseDate = releaseDate;
        else
            throw new ValidationException("дата релиза — не раньше " + simpleDateFormat.format(Date.valueOf(MINDATE)));
        if (duration > 0)
            this.duration = Duration.ofMinutes(duration);
        else
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        this.id = Objects.requireNonNullElseGet(id, () -> ++idSequence);
    }
}
