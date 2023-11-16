package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
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
    private String email;
    @NotBlank(message = "Login may not be empty")
    private String login;
    private String name;
    @Past(message = "Birthday mast be in the past")
    private LocalDate birthday;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private HashSet<Long> friendsList = new HashSet<>();


}
