package ru.yandex.practicum.sprint7.dto.courier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourierCreateDto {
    private String login;
    private String password;
    private String firstName;
}
