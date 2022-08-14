package ru.yandex.practicum.filmorate.model;

import lombok.*;

import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.validator.Login;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Validated
@Getter
@Setter
@ToString
@Builder
public class User {
    private Long id;
    @NotEmpty
    @Login
    private String login;
    private String name;
    @Email
    private String email;
    @Past
    private LocalDate birthday;
    private Set<Long> listFriends;
}
