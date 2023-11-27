package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User implements Update {
    @NotNull(groups = Update.class)
    private Long id;
    @Email(message = "Email doesn't match email pattern, example \"my@yandex.ru\"")
    @NotNull(message = "Email may not be empty")
    @Size(max = 100, message = "Email max string value 200 chars")
    private String email;
    @NotBlank(message = "Login may not be empty")
    @Size(max = 100, message = "Login max string value 200 chars")
    private String login;
    @Size(max = 100, message = "Name max string value 200 chars")
    private String name;
    @Past(message = "Birthday mast be in the past")
    private LocalDate birthday;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private HashSet<Long> friendsList = new HashSet<>();


}
