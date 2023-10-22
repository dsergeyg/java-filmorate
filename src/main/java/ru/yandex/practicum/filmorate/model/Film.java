package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder(setterPrefix = "set")
@Data
public class Film {
    private int id;
    @Size(min = 1, message = "Name may not be empty")
    @NotBlank(message = "Name may not be null")
    private String name;
    @Size(max = 200, message = "Description max string value 200 chars")
    private String description;
    @NotNull(message = "ReleaseDate may not be empty")
    private LocalDate releaseDate;
    @Positive(message = "Duration may not be negative")
    private long duration;
}
