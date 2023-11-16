package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Film implements Update {
    @NotNull(groups = Update.class)
    private long id;
    @NotBlank(message = "Name may not be null")
    private String name;
    @Size(max = 200, message = "Description max string value 200 chars")
    private String description;
    @NotNull(message = "ReleaseDate may not be empty")
    private LocalDate releaseDate;
    @Positive(message = "Duration may not be negative")
    private long duration;
    @NotNull(message = "Name may not be null")
    private Rating mpa;
    private HashSet<Long> genreList;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private HashSet<Long> likesList = new HashSet<>();
}
