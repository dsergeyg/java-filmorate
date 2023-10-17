package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Data
public class User {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static int idSequence = 0;
    private final int id;
    @Email(message = "Email должен иметь структуру my@yandex.ru")
    private String email;
    @NotBlank(message = "Login должен содержать символы")
    private final String login;
    @NotBlank(message = "Имя должно содержать символы")
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public User(Integer id, @NonNull String email, @NonNull String login, String name, LocalDate birthday) {
        if (id != null) {
            this.id = id;
        } else {
            this.id = idSequence++;
        }
        this.email = email;
        this.login = login;
        if(name != null) {
            this.name = name;
        } else {
            this.name = login;
        }
        this.birthday = birthday;
    }
}
