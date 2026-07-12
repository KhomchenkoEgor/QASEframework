package tests.ui;

import models.project.ProjectRq;
import org.testng.annotations.Test;
import utils.QwenDataGenerator;


public class ProjectTest extends BaseTest {

    @Test
    public void checkCreateProject() {
        ProjectRq projectData = QwenDataGenerator.generateProjectData();
        String projectName = projectData.getTitle();
        String projectCode = projectData.getCode();

        loginPage.openPage()
                .login(user, password)
                .isPageOpened()
                .createProject(projectName, projectCode)
                .checkProjectName(projectName)
                .returnToProjectsList()
                .deleteProject(projectName);
    }
}
