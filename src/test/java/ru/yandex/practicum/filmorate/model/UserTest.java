package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    @Test
    void validEmail() {
        assertDoesNotThrow(() -> new User(null, "my@yandex.ru", "SomeLogin", "SomeName", LocalDate.of(2000, 12, 31)));
        assertThrows(NullPointerException.class, () -> new User(null, null, "SomeLogin", "SomeName", LocalDate.of(2000, 12, 31)));
        assertThrows(ValidationException.class, () -> new User(null, "myyandex.ru", "SomeLogin", "SomeName", LocalDate.of(2000, 12, 31)));
    }

    @Test
    void validLogin() {
        assertDoesNotThrow(() -> new User(null, "my@yandex.ru", "SomeLogin", "SomeName", LocalDate.of(2000, 12, 31)));
        assertThrows(NullPointerException.class, () -> new User(null, "my@yandex.ru", null, "SomeName", LocalDate.of(2000, 12, 31)));
        Optional<ConstraintViolation<User>> violation = validator.validate(new User(null, "my@yandex.ru", "", "SomeName", LocalDate.of(2000, 12, 31))).stream().findFirst();
        assertFalse(violation.isEmpty());
        assertEquals("Login должен содержать символы", violation.get().getMessage());
    }

    @Test
    void validName() {
        String name = "SomeName";
        assertEquals(name, new User(null, "my@yandex.ru", "SomeLogin", name, LocalDate.of(2000, 12, 31)).getName());
        String login = "SomeLogin";
        assertEquals(login, new User(null, "my@yandex.ru", "SomeLogin", null, LocalDate.of(2000, 12, 31)).getName());
        assertEquals(login, new User(null, "my@yandex.ru", "SomeLogin", "", LocalDate.of(2000, 12, 31)).getName());
    }

    @Test
    void validBirthDay() {
        assertThrows(ValidationException.class, () -> new User(null, "my@yandex.ru", "SomeLogin", "SomeName", LocalDate.now().plusDays(1)));
        assertThrows(ValidationException.class, () -> new User(null, "my@yandex.ru", "SomeLogin", "SomeName", LocalDate.now()));
        assertDoesNotThrow(() -> new User(null, "my@yandex.ru", "SomeLogin", "SomeName", LocalDate.now().minusDays(1)));
    }

    @AfterAll
    static void factoryClose() {
        factory.close();
    }

}