package ru.yandex.practicum.sprint7.courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.sprint7.steps.CourierSteps;

import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

@DisplayName("Создание курьера")
public class CourierCreateTest {
    public static final String COURIER_PASSWORD = "strongpassword";
    public static final String COURIER_FIRST_NAME = "Вася";

    public final String courierLogin = UUID.randomUUID().toString();
    private final CourierSteps courierSteps = new CourierSteps();

    @Before
    public void setUp() {
        System.out.println("Courier login is " + courierLogin);
    }

    @After
    public void tearDown() {
        // Тут убираем за собой
        courierSteps.cleanupCourier(courierLogin, COURIER_PASSWORD);
    }

    @DisplayName("Создать курьера")
    @Description("Создание курьера в системе")
    @Test
    public void create() {
        courierSteps.create(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME)
                .then()
                .statusCode(HttpServletResponse.SC_CREATED)
                .assertThat()
                .body("ok", equalTo(true));
    }

    @DisplayName("Создать курьера второй раз с одинаковым логином")
    @Description("Повторное создание курьера в системе (одни и те же данные): ожидаем ошибку 409 Конфликт")
    @Test
    public void createTwice() {
        // Создаём курьера в первый раз
        courierSteps.create(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME);
        // Повторно создаём курьера
        courierSteps.create(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME)
                .then()
                .statusCode(HttpServletResponse.SC_CONFLICT)
                .assertThat()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @DisplayName("Создать курьера без указания логина")
    @Description("Создание курьера в системе без указания обязательного поля: логин")
    @Test
    public void createNoLogin() {
        courierSteps.create(null, COURIER_PASSWORD, COURIER_FIRST_NAME)
                .then()
                .statusCode(HttpServletResponse.SC_BAD_REQUEST)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @DisplayName("Создать курьера без указания пароля")
    @Description("Создание курьера в системе без указания обязательного поля: пароль")
    @Test
    public void createNoPassword() {
        courierSteps.create(courierLogin, null, COURIER_FIRST_NAME)
                .then()
                .statusCode(HttpServletResponse.SC_BAD_REQUEST)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
