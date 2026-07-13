package tests.api;

import adapters.CaseAdapter;
import adapters.ProjectAdapter;
import models.cases.CaseRq;
import models.cases.CaseRs;
import models.cases.Step;
import models.project.ProjectRq;
import org.testng.annotations.*;

import java.util.List;

import static org.testng.Assert.*;


public class CaseApiTest {

    private final String projectCode = "QA34" + (int)(Math.random() * 100000);
    private Integer caseId;

    @BeforeMethod
    public void createProjectBeforeTest() {
        ProjectRq projectRq = ProjectRq.builder()
                .title("Automation Project " + projectCode)
                .code(projectCode)
                .description("Временный проект для API тестов")
                .access("all")
                .build();

        var projectRs = ProjectAdapter.createProject(projectRq);
        assertTrue(projectRs.getStatus(), "Не удалось создать проект перед тестами!");
    }

    @Test
    public void checkCRUDCase() {
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
        ProjectAdapter.deleteProject(projectCode);
    }
}
