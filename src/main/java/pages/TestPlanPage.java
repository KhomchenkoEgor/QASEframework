package pages;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class TestPlanPage {

    private final String INITIATE_CREATE_PLAN_BUTTON = "#createButton";
    private final String PLAN_TITLE_INPUT = "form.form-create-plan input[name='title']";
    private final String ADD_CASES_BUTTON = "#edit-plan-add-cases-button";
    private final String SAVE_PLAN_BUTTON = "#save-plan";
    private final String FIRST_CASE_CHECKBOX = "//div[contains(@class,'modal') or contains(@class,'drawer') or @role='dialog']" +
            "//input[@type='checkbox']/ancestor::label | //input[@type='checkbox']/ancestor::label";
    private final String DONE_BUTTON = "//span[text()='Done']/ancestor::button";

    @Step("Создать тест-план с названием '{planTitle}' и привязать к нему сгенерированные кейсы")
    public TestPlanPage createTestPlan(String planTitle) {
        Configuration.clickViaJs = false;
        log.info("Инициализируем открытие формы создания тест-плана...");

        $(INITIATE_CREATE_PLAN_BUTTON).shouldBe(visible, java.time.Duration.ofSeconds(10)).click();
        $(PLAN_TITLE_INPUT).shouldBe(visible, java.time.Duration.ofSeconds(5)).setValue(planTitle);
        $(ADD_CASES_BUTTON).shouldBe(visible).click();
        sleep(500);
        $x(FIRST_CASE_CHECKBOX).shouldBe(visible, java.time.Duration.ofSeconds(5)).click();
        $x(DONE_BUTTON).shouldBe(visible, java.time.Duration.ofSeconds(5)).click();
        $(SAVE_PLAN_BUTTON).shouldBe(enabled).click();
        log.info("Тест-план успешно сохранен.");
        return this;
    }

    @Step("Проверить, что тест-план с названием '{planTitle}' успешно добавлен на страницу")
    public TestPlanPage checkPlanVisible(String planTitle) {
        $x(String.format("//a[contains(text(), '%s')] | //*[contains(text(), '%s')]", planTitle, planTitle))
                .shouldBe(visible, java.time.Duration.ofSeconds(5));
        return this;
    }

    @Step("Вернуться к списку проектов")
    public ProjectsPage returnToProjects() {
        open("/projects");
        return new ProjectsPage();
    }
}
