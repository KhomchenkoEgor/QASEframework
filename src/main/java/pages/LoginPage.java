package pages;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.shadowCss;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static dict.Elements.PROJECTS;
import static dict.Elements.SIGN_IN;


@Log4j2
public class LoginPage {

    private final String LOGIN="[name='email']";
    private final String PASSWORD="[name='password']";
    private final String GLOBAL_ERROR_ALERT = "div[role='alert'] span.mF0fFk";
    private final String FIELD_ERROR_MESSAGE = "div[role='alert'] span.mF0fFk";

    @Step("Открыть страницу авторизации")
    public LoginPage openPage() {
        open("/login");
        return this;
    }

    @Step("Выполнить вход в систему пользователем: '{user}'")
    public ProjectsPage login(String user, String password) {
        $(shadowCss("#accept", "#usercentrics-cmp-ui")).click();
        $(LOGIN).setValue(user);
        $(PASSWORD).setValue(password);
        $(byText(SIGN_IN)).click();
        $(byText(PROJECTS)).shouldBe(visible);
        return new ProjectsPage();
    }

    @Step("Выполнить попытку входа с невалидными данными пользователем: '{user}'")
    public LoginPage loginWithInvalidCredentials(String user, String password) {
        if ($(shadowCss("#accept", "#usercentrics-cmp-ui")).is(visible)) {
            $(shadowCss("#accept", "#usercentrics-cmp-ui")).click();
        }

        $(LOGIN).setValue(user);
        $(PASSWORD).setValue(password);
        $(byText(SIGN_IN)).click();

        return this;
    }

    @Step("Проверить отображение глобальной ошибки безопасности '{expectedError}'")
    public LoginPage checkGlobalError(String expectedError) {
        $(GLOBAL_ERROR_ALERT)
                .shouldBe(visible, java.time.Duration.ofSeconds(5))
                .shouldHave(text(expectedError));
        return this;
    }

    @Step("Проверить ошибку валидации формата поля: '{expectedError}'")
    public LoginPage checkFieldError(String expectedErrorPass) {
        $(FIELD_ERROR_MESSAGE)
                .shouldBe(visible, java.time.Duration.ofSeconds(5))
                .shouldHave(partialText(expectedErrorPass));
        return this;
    }
}

