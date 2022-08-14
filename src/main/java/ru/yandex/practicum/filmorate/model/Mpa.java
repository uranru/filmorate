package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Mpa {
    Integer id;
    String name;
}
