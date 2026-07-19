package tests.ui;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import listeners.TestListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import pages.LoginPage;
import pages.ProjectsPage;
import utils.PropertyReader;

import java.util.HashMap;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;

@Listeners({AllureTestNg.class, TestListener.class})
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseTest {

    LoginPage loginPage;
    ProjectsPage projectsPage;

    String user = System.getProperty("user", PropertyReader.getProperty("user"));
    String password = System.getProperty("password", PropertyReader.getProperty("password"));

    @BeforeMethod(alwaysRun = true, description = "Настройка драйвера")
    @Parameters({"browser"})
    @Step("Инициализация браузера {browser} и подготовка страниц")
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = "https://app.qase.io";
        Configuration.timeout = 30000;
        Configuration.clickViaJs = true;
        Configuration.browserSize = "1920x1080";

        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--headless");
        Configuration.browserCapabilities = options;

        loginPage = new LoginPage();
        projectsPage = new ProjectsPage();
    }

    @AfterMethod(alwaysRun = true, description = "Закрытие браузера")
    @Step("Закрытие сессии браузера")
    public void tearDown() {
        if (hasWebDriverStarted()) {
            getWebDriver().quit();
        }
    }
}
