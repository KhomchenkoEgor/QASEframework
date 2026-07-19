package tests.api;

import adapters.ProjectAdapter;
import adapters.SuiteAdapter;
import io.qameta.allure.*;
import lombok.extern.log4j.Log4j2;
import models.project.ProjectRq;
import models.suite.SuiteRq;
import models.suite.SuiteRs;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.QwenDataGenerator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Log4j2
@Epic("Qase API Engine")
@Feature("Тест-сьюты")
@Story("Иерархические структуры и каскадное управление")
@Owner("Khomchenko E.S.")
public class SuiteApiTest {

    private String projectCode;

    @BeforeMethod(alwaysRun = true)
    public void createProjectBeforeTest() {
        log.info("API Предусловие: Быстрое развертывание проекта");
        ProjectRq projectRq = QwenDataGenerator.generateProjectData();
        projectCode = projectRq.getCode();
        ProjectAdapter.createProject(projectRq);
    }

    @Test(
            testName = "Иерархия и каскадное удаление сьютов",
            description = "Проверка создания вложенных тест-сьютов (папок) и валидация связей по parent_id",
            groups = {"regression"})
    @Description("Тест проверяет корректность сборки древовидной структуры модулей. Генерация названий папок ведется моделью Qwen.")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("QASE-API-03")
    public void checkSuiteCrudLifecycle() {
        log.info("Тест: Сборка дерева тест-сьютов в репозитории: {}", projectCode);
        SuiteRq parentRq = QwenDataGenerator.generateSuiteData();
        SuiteRq childRq = QwenDataGenerator.generateSuiteData();
        SuiteRs parentRs = SuiteAdapter.createSuite(parentRq, projectCode);
        assertTrue(parentRs.getStatus(), "Не удалось создать родительский сьют через API!");
        int parentId = parentRs.getResult().getId();
        childRq.setParentId(parentId);
        SuiteRs childRs = SuiteAdapter.createSuite(childRq, projectCode);
        assertTrue(childRs.getStatus(), "Не удалось создать вложенный сьют через API!");
        int childId = childRs.getResult().getId();
        var getChildResponse = SuiteAdapter.getSuite(projectCode, childId);
        assertEquals(getChildResponse.jsonPath().getInt("result.parent_id"), parentId,
                "Дочерний сьют привязан к неверному родительскому ID!");
        assertEquals(getChildResponse.jsonPath().getString("result.title"), childRq.getTitle(),
                "Имя сьюта в базе не совпадает со сгенерированным ИИ!");
        SuiteAdapter.deleteSuite(projectCode, parentId);
        log.info("Каскадное удаление родительского ID {} и вложенного ID {} завершено.", parentId, childId);
    }

    @AfterMethod(alwaysRun = true)
    public void deleteProjectAfterTest() {
        log.info("API Постусловие: Удаление проекта {}", projectCode);
        ProjectAdapter.deleteProject(projectCode);
    }
}
