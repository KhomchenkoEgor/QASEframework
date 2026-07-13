package tests.api;

import adapters.ProjectAdapter;
import adapters.SuiteAdapter;
import models.project.ProjectRq;
import models.suite.SuiteRq;
import models.suite.SuiteRs;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.QwenDataGenerator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SuiteApiTest {

    private String projectCode;

    @BeforeMethod
    public void createProjectBeforeTest() {

        ProjectRq projectRq = QwenDataGenerator.generateProjectData();
        projectCode = projectRq.getCode();
        ProjectAdapter.createProject(projectRq);
    }

    @Test
    public void checkSuiteCrudLifecycle() {

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
    }

    @AfterMethod(alwaysRun = true)
    public void deleteProjectAfterTest() {
        ProjectAdapter.deleteProject(projectCode);
    }
}
