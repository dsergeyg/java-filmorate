package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(setterPrefix = "set")
public class User implements Update {
    @NotNull (groups = Update.class)
    private Long id;
    @Email(message = "Email doesn't match email pattern, example \"my@yandex.ru\"")
    @NotNull(message = "Email may not be empty")
    private String email;
    @NotBlank(message = "Login may not be empty")
    private final String login;
    private String name;
    @Past(message = "Birthday mast be in the past")
    private LocalDate birthday;
    @JsonIgnore
    private Set<Long> friendsList = new HashSet<>();
}
