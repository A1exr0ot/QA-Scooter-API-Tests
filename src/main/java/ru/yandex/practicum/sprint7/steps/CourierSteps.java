package ru.yandex.practicum.sprint7.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.sprint7.Endpoints;
import ru.yandex.practicum.sprint7.dto.courier.CourierCreateDto;
import ru.yandex.practicum.sprint7.dto.courier.IdDto;
import ru.yandex.practicum.sprint7.dto.courier.LoginPasswordDto;

public class CourierSteps extends BaseSteps {
    @Step("Создание курьера")
    public Response create(String login, String password, String firstName) {
        CourierCreateDto requestBody = new CourierCreateDto(
                login,
                password,
                firstName
        );
        return prepareRestSpec()
                .and().body(requestBody)
                .when()
                .post(Endpoints.COURIER);
    }

    @Step("Вход")
    public Response login(String login, String password) {
        LoginPasswordDto loginPasswordDto = new LoginPasswordDto(
                login, password
        );
        return prepareRestSpec()
                .and().body(loginPasswordDto)
                .post(Endpoints.COURIER_LOGIN);
    }

    @Step("Удаление курьера")
    public Response delete(Long id) {
        String idStr = id == null
                ? ""
                : id.toString();
        return prepareRestSpec().delete(Endpoints.COURIER + idStr);
    }

    @Step("Удалить курьера по логину, если он существует")
    public void cleanupCourier(String login, String password) {
        // 1. Логинимся и получаем ID
        Response response = login(login, password);
        if (response.statusCode() == 404) {
            // 2a. Если нет такого пользователя, то успешно тестируем
        } else if (response.statusCode() == 200) {
            // 2b. Если залогинились успешно, то удаляем по ID
            IdDto id = response.body().as(IdDto.class);
            delete(id.getId());
        } else {
            // 2c. Не можем обработать такую ситуацию
            throw new IllegalStateException("Courier can't be removed! Response code is " + response.statusCode());
        }
    }
}
