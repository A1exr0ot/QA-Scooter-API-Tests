package ru.yandex.practicum.sprint7.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.yandex.practicum.sprint7.Endpoints;
import ru.yandex.practicum.sprint7.dto.order.CreateOrderDto;
import ru.yandex.practicum.sprint7.dto.order.TrackIdDto;

import java.util.List;

public class OrderSteps extends BaseSteps {
    public static final CreateOrderDto ORDER = new CreateOrderDto(
            "Иван",
            "Петров",
            "Ленинградский проспект, 20а",
            "Динамо",
            "+7(123) 456-78-90",
            10,
            "2026-02-24",
            "Оставить у двери, не звонить",
            List.of("BLACK")
    );

    @Step("Создание заказа")
    public Response createOrder(List<String> colors) {
        CreateOrderDto request = ORDER.withColors(colors);

        return prepareRestSpec()
                .and().body(request)
                .post(Endpoints.ORDERS);
    }

    @Step("Получение заказов")
    public Response listOrders(
            Long courierId,
            List<String> nearestStations,
            Integer limit,
            Integer page
    ) {
        RequestSpecification spec = prepareRestSpec();
        if (courierId != null) {
            spec = spec.param("courierId", courierId);
        }
        if (nearestStations != null) {
            spec = spec.param("nearestStations", nearestStations);
        }
        if (limit != null) {
            spec = spec.param("limit", limit);
        }
        if (page != null) {
            spec = spec.param("page", page);
        }
        return spec.get(Endpoints.ORDERS);
    }

    @Step("Отменить заказ")
    public Response cancel(Integer track) {
        return prepareRestSpec()
                .and().body(new TrackIdDto(track))
                .put(Endpoints.ORDERS_CANCEL);
    }
}
