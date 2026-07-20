package tests.ui;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import listeners.TestListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import pages.LoginPage;
import pages.ProjectsPage;
import utils.PropertyReader;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Configuration.browser;
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
        browser = "chrome";
        Configuration.baseUrl = "https://app.qase.io";
        Configuration.timeout = 30000;
        Configuration.clickViaJs = true;
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            Map<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("credentials_enable_service", false);
            chromePrefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", chromePrefs);
            options.addArguments(
                    "--incognito",
                    "--disable-notifications",
                    "--disable-popup-blocking",
                    "--disable-infobars"
            );
            Configuration.browserCapabilities = options;
        } else if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            Configuration.browserCapabilities = options;
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            Configuration.browserCapabilities = options;
        }


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
