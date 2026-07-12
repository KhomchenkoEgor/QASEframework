package adapters;

import models.cases.CaseRq;
import models.cases.CaseRs;

import static io.restassured.RestAssured.given;


public class CaseAdapter extends BaseAdapter {

    public static CaseRs createCase(CaseRq caseRq, String projectCode) {
        return given()
                .spec(spec)
                .body(gson.toJson(caseRq))
                .log().all()
                .when()
                .post("/case/" + projectCode)
                .then()
                .log().all()
                .spec(ok200)
                .extract()
                .as(CaseRs.class);
    }

    public static CaseRs getCase(String projectCode, Integer caseId) {
        return given()
                .spec(spec)
                .when()
                .log().all()
                .get("/case/" + projectCode + "/" + caseId)
                .then()
                .log().all()
                .spec(ok200)
                .extract()
                .as(CaseRs.class);
    }

    public static CaseRs updateCase(CaseRq caseRq, String projectCode, Integer caseId) {
        return given()
                .spec(spec)
                .body(gson.toJson(caseRq))
                .log().all()
                .when()
                .patch("/case/" + projectCode + "/" + caseId)
                .then()
                .log().all()
                .spec(ok200)
                .extract()
                .as(CaseRs.class);
    }

    public static CaseRs deleteCase(String projectCode, Integer caseId) {
        return given()
                .spec(spec)
                .when()
                .delete("/case/" + projectCode + "/" + caseId)
                .then()
                .log().all()
                .spec(ok200)
                .extract()
                .as(CaseRs.class);
    }
}
