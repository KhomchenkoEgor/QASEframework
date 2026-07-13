package tests.ui;


import org.testng.annotations.Test;
import utils.QwenDataGenerator;

public class LoginTest extends BaseTest {

    @Test(description = "Успешная авторизация с валидными учетными данными")
    public void checkSuccessfulLogin() {
        loginPage.openPage()
                .login(user, password)
                .isPageOpened();
    }

    @Test(description = "Авторизация с неверным паролем")
    public void checkLoginWithWrongPassword() {
        String wrongPassword = "Wrong_Password_" + QwenDataGenerator.generateText(5);

        loginPage.openPage()
                .loginWithInvalidCredentials(user, wrongPassword)
                .checkGlobalError("These credentials do not match our records.");
    }

    @Test(description = "Авторизация с некорректным форматом email")
    public void checkLoginWithInvalidEmailFormat() {
        String randomEmail = QwenDataGenerator.generateEmail();
        loginPage.openPage()
                .loginWithInvalidCredentials(randomEmail, password)
                .checkFieldError("does not match format email");
    }
}
