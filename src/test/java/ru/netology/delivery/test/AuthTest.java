package ru.netology.delivery.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

class AuthTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldActiveUserLogin() {
        val validUser = DataGenerator.Registration.generateActiveUser();
        $("[type='text']").doubleClick().sendKeys(validUser.getLogin());
        $("[type='password']").doubleClick().sendKeys(validUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldBlockedUserLogin() {
        val invalidUser = DataGenerator.Registration.generateBlockedUser();
        $("[type='text']").doubleClick().sendKeys(invalidUser.getLogin());
        $("[type='password']").doubleClick().sendKeys(invalidUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Пользователь заблокирован")).shouldBe(visible);
    }

    @Test
    void shouldEnterInvalidLogin() {
        val validUser = DataGenerator.Registration.generateActiveUser();
        $("[type='text']").doubleClick().sendKeys(DataGenerator.generateLogin());
        $("[type='password']").doubleClick().sendKeys(validUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    void shouldEnterInvalidPassword() {
        val validUser = DataGenerator.Registration.generateActiveUser();
        $("[type='text']").doubleClick().sendKeys(validUser.getLogin());
        $("[type='password']").doubleClick().sendKeys(DataGenerator.generatePassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    void shouldEmptyLoginField() {
        val validUser = DataGenerator.Registration.generateActiveUser();
        $("[type='password']").doubleClick().sendKeys(validUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='login'] .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldEmptyPasswordField() {
        val validUser = DataGenerator.Registration.generateActiveUser();
        $("[type='text']").doubleClick().sendKeys(validUser.getLogin());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='password'] .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSubmittingEmptyForm() {
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='login'] .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
        $("[data-test-id='password'] .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }
}


