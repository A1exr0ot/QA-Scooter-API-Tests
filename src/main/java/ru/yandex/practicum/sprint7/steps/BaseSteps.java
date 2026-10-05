package ru.yandex.practicum.sprint7.steps;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseSteps {
    protected RequestSpecification prepareRestSpec() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        return given()
                .header("Content-type", "application/json");
    }
}
