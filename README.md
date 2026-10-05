# API-тесты для сервиса «Яндекс Самокат»

Проект по автоматизации тестирования REST API сервиса [«Яндекс Самокат»](https://qa-scooter.praktikum-services.ru) на Java.

## 📋 Содержание

- [Стек технологий](#-стек-технологий)
- [Что тестируется](#-что-тестируется)
- [Структура проекта](#-структура-проекта)
- [Запуск тестов](#-запуск-тестов)
- [Allure-отчёт](#-allure-отчёт)
- [Автор](#-автор)

---

## 🛠 Стек технологий

| Технология | Версия | Назначение |
|---|---|---|
| Java | 11 | Язык разработки |
| Maven | — | Сборка проекта |
| JUnit | 4.13.2 | Тестовый фреймворк |
| Rest Assured | 4.4.0 | Тестирование REST API |
| Allure | 2.23.0 | Отчётность |
| Lombok | 1.18.30 | Генерация DTO |
| Gson | 2.8.9 | Сериализация JSON |
| javax.servlet-api | 4.0.1 | HTTP-коды ответов |

---

## 🎯 Что тестируется

### 1. Создание курьера (`/api/v1/courier`)
- ✅ Курьера можно создать → `201 Created`, `ok: true`
- ✅ Нельзя создать двух курьеров с одинаковым логином → `409 Conflict`
- ✅ Без логина → `400 Bad Request`
- ✅ Без пароля → `400 Bad Request`

### 2. Логин курьера (`/api/v1/courier/login`)
- ✅ Успешная авторизация → `200 OK`, возвращается `id`
- ✅ Без логина → `400 Bad Request`
- ✅ Без пароля → `400 Bad Request`
- ✅ Неверный пароль → `404 Not Found`
- ✅ Несуществующий логин → `404 Not Found`

### 3. Создание заказа (`/api/v1/orders`)
- ✅ Параметризация по цветам: `[BLACK, GREY]`, `[BLACK]`, `[GREY]`, `[]`
- ✅ В ответе возвращается `track` → `201 Created`

### 4. Список заказов (`/api/v1/orders`)
- ✅ Список заказов возвращается в теле ответа → `200 OK`
- ✅ Работает параметр `limit`

### 5. Удаление курьера (`/api/v1/courier/:id`)
- ✅ Удаление существующего курьера → `200 OK`, `ok: true`
- ✅ Удаление несуществующего → `404 Not Found`
- ✅ Удаление без `id` → `400 Bad Request`

---

## 📁 Структура проекта

```
Sprint_7/
├── src/
│   ├── main/java/ru/yandex/practicum/sprint7/
│   │   ├── Endpoints.java              # Константы URL-эндпоинтов
│   │   ├── dto/                        # DTO для запросов/ответов
│   │   │   ├── courier/
│   │   │   │   ├── CourierCreateDto.java
│   │   │   │   ├── IdDto.java
│   │   │   │   └── LoginPasswordDto.java
│   │   │   └── order/
│   │   │       ├── CreateOrderDto.java
│   │   │       └── TrackIdDto.java
│   │   └── steps/                      # Шаги (обёртки над Rest Assured)
│   │       ├── BaseSteps.java
│   │       ├── CourierSteps.java
│   │       └── OrderSteps.java
│   └── test/java/ru/yandex/practicum/sprint7/
│       ├── courier/
│       │   ├── CourierCreateTest.java
│       │   ├── CourierDeleteTest.java
│       │   └── CourierLoginTest.java
│       └── order/
│           ├── OrderCreateTest.java
│           └── OrdersListTest.java
├── pom.xml
└── README.md
```

**Архитектура:**
- **Steps** — слой с методами для вызова API, аннотирован `@Step` для Allure.
- **DTO** — классы для сериализации/десериализации JSON (Lombok).
- **Endpoints** — единое место хранения URL-путей.
- **Tests** — тестовые классы с параметризацией и `@DisplayName` для читаемых отчётов.

---

## 🚀 Запуск тестов

### Требования
- JDK 11+
- Maven 3.6+

### Клонирование репозитория
```bash
git clone https://github.com/A1exr0ot/Sprint_7.git
cd Sprint_7
```

### Запуск всех тестов
```bash
mvn clean test
```

### Запуск конкретного класса
```bash
mvn clean test -Dtest=CourierCreateTest
```

### Запуск конкретного метода
```bash
mvn clean test -Dtest=CourierCreateTest#create
```

---

## 📊 Allure-отчёт

### Генерация и открытие отчёта
```bash
mvn clean test
mvn allure:serve
```

Отчёт автоматически откроется в браузере. Результаты сохраняются в `target/allure-results`, отчёт — в `target/allure-report`.

### Просмотр готового отчёта
```bash
mvn allure:report
```

> ⚠️ **Папка `target/` не коммитится.** Чтобы добавить в репозиторий только готовый отчёт, используйте отдельную ветку или папку `allure-report` (см. `.gitignore`).

---

## 🔧 Особенности реализации

- **Параметризация** в `OrderCreateTest` через `@RunWith(Parameterized.class)` — покрыты все варианты цветов заказа.
- **Cleanup** — после каждого теста данные удаляются через `@After` (отмена заказа, удаление курьера).
- **UUID** для логина курьера — исключает конфликты между запусками.
- **`BaseSteps`** — единая точка настройки `RestAssured` (baseURI, Content-Type).

---

## 🐛 Известные ограничения

- Тесты рассчитаны на **последовательный** запуск (используется статический `RestAssured.baseURI`).
- `OrdersListTest` создаёт 30 заказов в `@Before` — прогон занимает время.
- Java 11 и Rest Assured 4.4.0 — актуальные на момент обучения версии.

---

## 👤 Автор

**Александр Кораблев**
- GitHub: [@A1exr0ot](https://github.com/A1exr0ot)
- Email: xraid555@gmail.com
- Telegram: [@exp1oIt_r0ot](https://t.me/exp1oIt_r0ot)
