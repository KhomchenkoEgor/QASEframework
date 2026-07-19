package tests.api;

import adapters.ProjectAdapter;
import io.qameta.allure.*;
import lombok.extern.log4j.Log4j2;
import models.project.ProjectRq;
import models.project.ProjectRs;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static adapters.ProjectAdapter.createProject;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Log4j2
@Epic("Qase API Engine")
@Feature("Проекты")
@Story("Управление жизненным циклом репозитория")
@Owner("Khomchenko E.S.")
public class ProjectApiTest {

    private  final String CODE = "QA";

    @Test(
            testName = "Создание проекта через API",
            description = "Проверка успешного создания нового репозитория проекта с помощью ИИ данных",
            groups = {"smoke", "regression"})
    @Description("Тест запрашивает уникальные параметры (имя, код) у локальной LLM Qwen, отправляет POST запрос и валидирует ответ бэкенда")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("QASE-API-01")
    public void checkCreateProject() {
        log.info("Тест: Запуск сценария генерации и создания проекта через API");
        ProjectRq rq = ProjectRq.builder()
                .title("QA34")
                .code(CODE)
                .description("test")
                .access("all")
                .group("test")
                .build();

        ProjectRs rs = createProject(rq);
        assertTrue(rs.status);
        assertEquals(rs.result.code, "QA");
    }

    @AfterMethod
    public  void deleteProject(){
        log.info("API Очистка: Каскадное удаление временного проекта с кодом: {}", CODE);
        ProjectAdapter.deleteProject(CODE);
    }
}
