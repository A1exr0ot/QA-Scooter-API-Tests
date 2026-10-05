package ru.yandex.practicum.sprint7.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.sprint7.dto.order.TrackIdDto;
import ru.yandex.practicum.sprint7.steps.OrderSteps;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Получение списка заказов")
public class OrdersListTest {
    public static final String COLOR_BLACK = "BLACK";
    public static final int DEFAULT_ORDERS_PER_PAGE = 30;

    private final OrderSteps orderSteps = new OrderSteps();
    private List<TrackIdDto> createdOrders;

    @Before
    public void setUp() {
        createdOrders = new ArrayList<>();
        // На всякий случай создадим заказы, вдруг их сейчас нет в тестовой базе
        for (int i = 0; i < DEFAULT_ORDERS_PER_PAGE; i++) {
            createdOrders.add(
                    orderSteps.createOrder(List.of(COLOR_BLACK)).body().as(TrackIdDto.class)
            );
        }
    }

    @After
    public void tearDown() {
        for (TrackIdDto track : createdOrders) {
            orderSteps.cancel(track.getTrack());
        }
    }

    @DisplayName("Список заказов с ограничением 1")
    @Description("Получение первой страницы с ограничением 1 заказ на страницу")
    @Test
    public void listOrdersWithLimit() {
        orderSteps.listOrders(null, null, 1, 0)
                .then()
                .body("orders", notNullValue())
                .body("orders", hasSize(1))
                .statusCode(HttpServletResponse.SC_OK);
    }

    @DisplayName("Список заказов без ограничения")
    @Description("Получение первой страницы с ограничением по-умолчанию 30 заказов на страницу")
    @Test
    public void listOrders() {
        orderSteps.listOrders(null, null, null, null)
                .then()
                .body("orders", notNullValue())
                .body("orders", hasSize(DEFAULT_ORDERS_PER_PAGE))
                .statusCode(HttpServletResponse.SC_OK);
    }
}
