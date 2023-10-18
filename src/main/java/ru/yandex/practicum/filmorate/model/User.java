package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Objects;

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

    public User(Integer id, @NonNull String email, @NonNull String login, String name, LocalDate birthday) throws ValidationException {
        this.email = email;
        if (!login.contains(" "))
            this.login = login;
        else
            throw new ValidationException("Login не должен содержать пробелы");
        if (name != null)
            this.name = Objects.requireNonNullElse(name.isBlank() ? null : name, login);
        else
            this.name = login;
        this.birthday = birthday;
        this.id = Objects.requireNonNullElseGet(id, () -> ++idSequence);
    }
}
