package tests.api;

import adapters.CaseAdapter;
import adapters.PlanAdapter;
import adapters.ProjectAdapter;
import models.cases.CaseRq;
import models.plan.PlanRq;
import models.plan.PlanRs;
import models.project.ProjectRq;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.QwenDataGenerator;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestPlanApiTest {

    private String projectCode;
    private Integer caseId;

    @BeforeMethod
    public void prepareData() {

        ProjectRq projectRq = QwenDataGenerator.generateProjectData();
        projectCode = projectRq.getCode();
        ProjectAdapter.createProject(projectRq);

        CaseRq caseRq = QwenDataGenerator.generateTestCaseData();
        var caseRs = CaseAdapter.createCase(caseRq, projectCode);
        caseId = caseRs.getResult().getId();
    }

    @Test
    public void checkTestPlanCrudLifecycle() {

        PlanRq planRq = QwenDataGenerator.generatePlanData(List.of(caseId));

        PlanRs planRs = PlanAdapter.createPlan(planRq, projectCode);
        assertTrue(planRs.getStatus(), "Не удалось создать тест-план через API!");
        int planId = planRs.getResult().getId();

        var getPlanResponse = PlanAdapter.getPlan(projectCode, planId);
        assertEquals(getPlanResponse.jsonPath().getString("result.title"), planRq.getTitle(),
                "Название тест-плана не совпадает со сгенерированным ИИ!");
        assertEquals(getPlanResponse.jsonPath().getList("result.cases.case_id").get(0), caseId,
                "ID тест-кейса внутри плана сохранился некорректно!");

        PlanAdapter.deletePlan(projectCode, planId);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        ProjectAdapter.deleteProject(projectCode);
    }
}