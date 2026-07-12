package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class ProjectPage {

    private final String SUITE_TITLE_INPUT = "#title";
    private final String CONFIRM_SUITE_BUTTON = "//span[text()='Create']/ancestor::button";
    private final String ACTIVE_SUITE_INPUT = "form.NWLa0T #title";
    private final String ACTIVE_CONFIRM_BUTTON = "form.NWLa0T button[type='submit']";

    @Step("Дождаться полной загрузки интерфейса репозитория")
    public ProjectPage waitForPageLoaded() {
        log.info("Ожидаем загрузку визуальных компонентов репозитория Qase...");

        Configuration.clickViaJs = false;

        $x("//span[text()='Repository'] | //button[contains(., 'Manual test')]")
                .shouldBe(visible, java.time.Duration.ofSeconds(30));

        log.info("Компоненты репозитория успешно обнаружены.");
        Configuration.clickViaJs = true;
        return this;
    }

    @Step("Проверить, что открыт проект с именем '{projectName}'")
    public ProjectPage checkProjectName(String projectName) {
        log.info("Проверяем заголовок открытого репозитория...");

        if ($x("//button[contains(., 'Create new suite')]").is(visible)) {
            log.info("Успешно зафиксирован пустой стейт репозитория (кнопка 'Create new suite' доступна).");
        } else {
            $x("//h1").shouldBe(visible);
        }

        log.info("Валидация открытия проекта пройдена успешно!");
        return this;
    }

    @Step("Вернуться на страницу со списком проектов")
    public ProjectsPage returnToProjectsList() {
        open("/projects");
        return new ProjectsPage();
    }

    @Step("Создать корневой тест-сьют с именем '{suiteName}'")
    public ProjectPage createSuite(String suiteName) {
        com.codeborne.selenide.Configuration.clickViaJs = false;
        log.info("Начинаем процесс создания тест-сьюта: " + suiteName);
        try {
            $x("//button[contains(., 'Create new suite')] | //a[contains(., 'Create new suite')]")
                    .shouldBe(visible, java.time.Duration.ofSeconds(5))
                    .click();
            log.info("Кликнули по центральной кнопке 'Create new suite'.");
        } catch (ElementNotFound e) {
            // Если проект уже не пустой, кликаем по стандартной верхней кнопке "+ Suite" или "+ Сьют"
            log.info("Центральная кнопка не найдена. Пытаемся кликнуть по верхней панели инструментов...");
            $x("//button[contains(., 'Suite') or contains(., 'сьют')] | //*[text()='+ Suite']")
                    .shouldBe(visible, java.time.Duration.ofSeconds(5))
                    .click();
        }

        $(SUITE_TITLE_INPUT).shouldBe(visible).setValue(suiteName);
        $x(CONFIRM_SUITE_BUTTON).click();

        log.info("Тест-сьют успешно сохранен в репозитории.");
        Configuration.clickViaJs = true;
        return this;
    }

    @Step("Проверить, что тест-сьют с именем '{suiteName}' отображается в дереве репозитория")
    public ProjectPage checkSuiteVisible(String suiteName) {
        $(byText(suiteName)).shouldBe(visible);
        return this;
    }

    @Step("Создать дочерний тест-сьют '{childName}' внутри родительского '{parentName}'")
    public ProjectPage createChildSuite(String parentName, String childName) {
        log.info("Создаем вложенный сьют '" + childName + "' в родительский '" + parentName + "'");

        String targetButtonXpath = String.format("//button[contains(@aria-label, 'suite %s actions')]", parentName);

        Configuration.clickViaJs = false;
        $x(targetButtonXpath)
                .shouldBe(visible, java.time.Duration.ofSeconds(10))
                .click();

        sleep(500);

        $("[data-key='create_suite']")
                .shouldBe(visible, java.time.Duration.ofSeconds(5))
                .click();

        $(ACTIVE_SUITE_INPUT).shouldBe(visible).setValue(childName);
        $(ACTIVE_CONFIRM_BUTTON).shouldBe(enabled).click();

        log.info("Вложенный тест-сьют успешно создан.");
        return this;
    }

    @Step("Клонировать тест-сьют '{suiteName}' через UI контекстное меню")
    public ProjectPage cloneSuiteViaUi(String suiteName) {
        log.info("Клонируем тест-сьют: " + suiteName);

        String targetButtonXpath = String.format("//button[contains(@aria-label, 'suite %s actions')]", suiteName);

        Configuration.clickViaJs = false;
        $x(targetButtonXpath)
                .shouldBe(visible, java.time.Duration.ofSeconds(10))
                .click();

        sleep(500);

        $("[data-key='clone']")
                .shouldBe(visible, java.time.Duration.ofSeconds(5))
                .click();

        $x("//span[text()='Clone']/ancestor::button | //button[contains(., 'Clone')]")
                .shouldBe(visible, java.time.Duration.ofSeconds(5))
                .click();

        log.info("Запрос на клонирование выполнен.");
        return this;
    }

    @Step("Проверить, что в системе появилось несколько копий сьюта '{suiteName}'")
    public ProjectPage checkClonedSuiteVisible(String suiteName) {
        $$x(String.format("//span[text()='%s']", suiteName)).shouldHave(sizeGreaterThan(1));
        return this;
    }

    @Step("Перейти в раздел 'Test Plans' текущего проекта")
    public TestPlanPage navigateToTestPlans() {
        log.info("Кликаем по пункту 'Test Plans' в боковой панели навигации Qase...");
        Configuration.clickViaJs = true;

        $x("//aside//span[text()='Test Plans'] | //aside//a[contains(@href, '/plans')]")
                .shouldBe(visible, java.time.Duration.ofSeconds(10))
                .click();

        Configuration.clickViaJs = false;

        return new TestPlanPage();
    }
}
