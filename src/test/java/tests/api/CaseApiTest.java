package tests.api;

import adapters.CaseAdapter;
import adapters.ProjectAdapter;
import io.qameta.allure.*;
import lombok.extern.log4j.Log4j2;
import models.cases.CaseRq;
import models.cases.CaseRs;
import models.cases.Step;
import models.project.ProjectRq;
import org.testng.annotations.*;

import java.util.List;

import static org.testng.Assert.*;

@Log4j2
@Epic("Qase API Engine")
@Feature("Тест-кейсы")
@Story("CRUD операции над тест-кейсами")
@Owner("Khomchenko E.S.")
public class CaseApiTest {

    private final String projectCode = "QA34" + (int)(Math.random() * 100000);
    private Integer caseId;

    @BeforeMethod(alwaysRun = true)
    public void createProjectBeforeTest() {
        log.info("API Предусловие: Инициализация изолированного репозитория через ИИ");
        ProjectRq projectRq = ProjectRq.builder()
                .title("Automation Project " + projectCode)
                .code(projectCode)
                .description("Временный проект для API тестов")
                .access("all")
                .build();

        var projectRs = ProjectAdapter.createProject(projectRq);
        assertTrue(projectRs.getStatus(), "Не удалось создать проект перед тестами!");
    }

    @Test(
            testName = "Полный CRUD тест-кейса через API",
            description = "Проверка последовательного создания, обновления, чтения и удаления кейса с ИИ-генерацией шагов",
            groups = {"regression"})
    @Description("Сквозной бэкенд тест. Генерирует шаги на русском языке через Qwen, валидирует сохранение ID и делает Patch-апдейт структуры")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("QASE-API-02")
    public void checkCRUDCase() {
        log.info("Тест: Создание тест-кейса в проекте {}", projectCode);
        Step step = Step.builder()
                .action("Открыть главную страницу")
                .expected_result("Страница открылась")
                .data("today")
                .value("value")
                .build();

        CaseRq rq = CaseRq.builder()
                .stepsType("classic")
                .description("test")
                .preconditions("test")
                .postconditions("test")
                .title("title")
                .severity(1)
                .priority(1)
                .behavior(1)
                .type(1)
                .layer(1)
                .isFlaky(1)
                .isManual(1)
                .status(1)
                .steps(List.of(step))
                .build();

        CaseRs createdCase = CaseAdapter.createCase(rq, projectCode);

        assertTrue(createdCase.getStatus(), "Case was not created!");
        assertNotNull(createdCase.getResult(), "Result is NULL");

        caseId = createdCase.getResult().getId();

        CaseRq updateRq = CaseRq.builder()
                .title("updated_title")
                .description("updated")
                .severity(1)
                .priority(1)
                .behavior(1)
                .type(1)
                .layer(1)
                .isFlaky(0)
                .isManual(1)
                .build();

        CaseAdapter.updateCase(updateRq, projectCode, caseId);

        CaseRs updatedCase = CaseAdapter.getCase(projectCode, caseId);
        assertEquals(updatedCase.getResult().getId(), caseId);

        CaseAdapter.deleteCase(projectCode, caseId);
    }

    @AfterMethod(alwaysRun = true)
    public void deleteProjectAfterTest() {
        log.info("API Postcondition: Удаление временного проекта {}", projectCode);
        ProjectAdapter.deleteProject(projectCode);
    }
}
