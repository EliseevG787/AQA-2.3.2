package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    // спецификация нужна для того, чтобы переиспользовать настройки в разных запросах
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static void sendRequest(UserInfo user) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    private static final Faker faker = new Faker(new Locale("en"));

    public static String generateLogin() {
        return faker.name().username();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String status) {
            UserInfo user = new UserInfo(
                    generateLogin(),
                    generatePassword(),
                    status
            );
            sendRequest(user);
            return user;
        }

        public static UserInfo generateActiveUser() {
            return generateUser("active");
        }

        public static UserInfo generateBlockedUser() {
            return generateUser("blocked");
        }
    }

    @Value
    public static class UserInfo {
        private String login;
        private String password;
        private String status;
    }
}

