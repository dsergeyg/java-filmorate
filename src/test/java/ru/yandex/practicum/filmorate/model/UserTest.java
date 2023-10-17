package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

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
        Optional<ConstraintViolation<User>> violation = validator.validate(new User(null, "myyandex.ru", "SomeLogin", "SomeName", LocalDate.of(2000, 12, 31))).stream().findFirst();
        assertFalse(violation.isEmpty());
        assertEquals("Email должен иметь структуру my@yandex.ru", violation.get().getMessage());
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
        Optional<ConstraintViolation<User>> violation = validator.validate(new User(null, "my@yandex.ru", "SomeLogin", "", LocalDate.of(2000, 12, 31))).stream().findFirst();
        assertFalse(violation.isEmpty());
        assertEquals("Имя должно содержать символы", violation.get().getMessage());
    }

    @Test
    void validBirthDay() {
        Optional<ConstraintViolation<User>> violation = validator.validate(new User(null, "my@yandex.ru", "SomeLogin", "SomeName", LocalDate.now().plusDays(1))).stream().findFirst();
        assertFalse(violation.isEmpty());
        assertEquals("Дата рождения не может быть в будущем", violation.get().getMessage());
        violation = validator.validate(new User(null, "my@yandex.ru", "SomeLogin", "SomeName", LocalDate.now())).stream().findFirst();
        assertFalse(violation.isEmpty());
        assertEquals("Дата рождения не может быть в будущем", violation.get().getMessage());
        violation = validator.validate(new User(null, "my@yandex.ru", "SomeLogin", "SomeName", LocalDate.now().minusDays(1))).stream().findFirst();
        assertTrue(violation.isEmpty());
    }

    @AfterAll
    static void factoryClose() {
        factory.close();
    }

}