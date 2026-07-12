package tests.ui;

import models.project.ProjectRq;
import models.suite.SuiteRq;
import org.testng.annotations.Test;
import utils.QwenDataGenerator;

public class SuiteUiTest extends BaseTest {

    @Test
    public void checkNestedSuiteAndCloningLifecycle() {
        ProjectRq projectData = QwenDataGenerator.generateProjectData();
        SuiteRq parentSuiteData = QwenDataGenerator.generateSuiteData();
        SuiteRq childSuiteData = QwenDataGenerator.generateSuiteData();

        loginPage.openPage()
                .login(user, password)
                .isPageOpened()
                .createProject(projectData.getTitle(), projectData.getCode())
                .createSuite(parentSuiteData.getTitle())
                .checkSuiteVisible(parentSuiteData.getTitle())
                .createChildSuite(parentSuiteData.getTitle(), childSuiteData.getTitle())
                .checkSuiteVisible(childSuiteData.getTitle())
                .cloneSuiteViaUi(parentSuiteData.getTitle())
                .checkClonedSuiteVisible(parentSuiteData.getTitle())
                .returnToProjectsList()
                .deleteProject(projectData.getTitle());
    }
}