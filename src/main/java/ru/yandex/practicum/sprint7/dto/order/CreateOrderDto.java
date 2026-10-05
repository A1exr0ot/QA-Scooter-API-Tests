package ru.yandex.practicum.sprint7.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class CreateOrderDto {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    // Не обязательное поле
    private List<String> color;

    public CreateOrderDto withColors(List<String> colors) {
        return this.toBuilder().color(colors).build();
    }
}
