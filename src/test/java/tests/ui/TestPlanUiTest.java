package tests.ui;

import adapters.CaseAdapter;
import adapters.ProjectAdapter;
import models.cases.CaseRq;

import models.project.ProjectRq;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.QwenDataGenerator;


public class TestPlanUiTest extends BaseTest {

    private String projectCode;
    private String projectName;

    @BeforeMethod
    public void prepareDataViaApi() {
        ProjectRq projectRq = QwenDataGenerator.generateProjectData();
        projectCode = projectRq.getCode();
        projectName = projectRq.getTitle();
        ProjectAdapter.createProject(projectRq);

        CaseRq caseRq = QwenDataGenerator.generateTestCaseData();
        CaseAdapter.createCase(caseRq, projectCode);
    }

    @Test
    public void checkCreateTestPlanViaUi() {
        String planTitle = QwenDataGenerator.generatePlanTitleViaLlm();

        loginPage.openPage()
                .login(user, password)
                .isPageOpened();

        projectsPage.openPage(projectName)
                .navigateToTestPlans()
                .createTestPlan(planTitle)
                .checkPlanVisible(planTitle)
                .returnToProjects();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupApi() {
        ProjectAdapter.deleteProject(projectCode);
    }
}