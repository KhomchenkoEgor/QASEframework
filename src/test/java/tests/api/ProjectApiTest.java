package tests.api;

import adapters.ProjectAdapter;
import models.project.ProjectRq;
import models.project.ProjectRs;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static adapters.ProjectAdapter.createProject;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ProjectApiTest {

    private  final String CODE = "QA";

    @Test
    public void checkCreateProject() {
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
        ProjectAdapter.deleteProject(CODE);
    }
}
