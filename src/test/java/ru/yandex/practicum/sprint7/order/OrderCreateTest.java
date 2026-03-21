package ru.yandex.practicum.sprint7.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.sprint7.dto.order.TrackIdDto;
import ru.yandex.practicum.sprint7.steps.OrderSteps;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.isA;


@DisplayName("Создание заказа")
@RunWith(Parameterized.class)
public class OrderCreateTest {
    public static final String COLOR_BLACK = "BLACK";
    public static final String COLOR_GREY = "GREY";

    private final OrderSteps orderSteps;
    private final List<String> color;
    private Response order;

    public OrderCreateTest(List<String> color) {
        this.orderSteps = new OrderSteps();
        this.color = color;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] getSuitableColors() {
        return new Object[][]{
                {List.of(COLOR_BLACK, COLOR_GREY)},
                {List.of(COLOR_BLACK)},
                {List.of(COLOR_GREY)},
                {Collections.emptyList()},
                {Collections.singletonList(null)},
        };
    }

    @After
    public void tearDown() {
        TrackIdDto track = order.body().as(TrackIdDto.class);
        orderSteps.cancel(track.getTrack());
    }

    @DisplayName("Создать заказ")
    @Description("Создание заказа в системе. Параметризуется списком цветов.")
    @Test
    public void createOrder() {
        order = orderSteps.createOrder(color);
        order
                .then()
                .statusCode(HttpServletResponse.SC_CREATED)
                .body("track", anyOf(
                        isA(Long.class),
                        isA(Integer.class)
                ));
    }
}
