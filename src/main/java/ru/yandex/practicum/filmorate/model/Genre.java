package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Genre {
    @NotBlank(message = "may not by null or empty!")
    long id;
    @Size(min = 1, max = 100, message = "Description max string value 100 chars")
    String name;
}
