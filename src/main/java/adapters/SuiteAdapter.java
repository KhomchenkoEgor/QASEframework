package adapters;

import models.suite.SuiteRq;
import models.suite.SuiteRs;
import static io.restassured.RestAssured.given;

public class SuiteAdapter extends BaseAdapter {

    public static SuiteRs createSuite(SuiteRq suiteRq, String projectCode) {
        return given()
                .spec(spec)
                .body(gson.toJson(suiteRq))
                .log().all()
                .when()
                .post("/suite/" + projectCode)
                .then()
                .log().all()
                .spec(ok200)
                .extract()
                .as(SuiteRs.class);
    }

    public static io.restassured.response.Response getSuite(String projectCode, Integer suiteId) {
        return given()
                .spec(spec)
                .when()
                .log().all()
                .get("/suite/" + projectCode + "/" + suiteId)
                .then()
                .log().all()
                .spec(ok200)
                .extract()
                .response();
    }

    public static SuiteRs deleteSuite(String projectCode, Integer suiteId) {
        return given()
                .spec(spec)
                .when()
                .log().all()
                .delete("/suite/" + projectCode + "/" + suiteId)
                .then()
                .log().all()
                .spec(ok200)
                .extract()
                .as(SuiteRs.class);
    }
}
