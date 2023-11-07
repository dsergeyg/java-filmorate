package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder(setterPrefix = "set")
public class User implements Update {
    @NotNull (groups = Update.class)
    private Integer id;
    @Email(message = "Email doesn't match email pattern, example \"my@yandex.ru\"")
    @NotNull(message = "Email may not be empty")
    private String email;
    @NotNull (message = "Login may not be empty")
    private final String login;
    private String name;
    @Past(message = "Birthday mast be in the past")
    private LocalDate birthday;
}
