package adapters;

import models.plan.PlanRq;
import models.plan.PlanRs;
import static io.restassured.RestAssured.given;

public class PlanAdapter extends BaseAdapter {

    public static PlanRs createPlan(PlanRq planRq, String projectCode) {
        return given()
                .spec(spec)
                .body(gson.toJson(planRq))
                .log().all()
                .when()
                .post("/plan/" + projectCode)
                .then()
                .log().all()
                .spec(ok200)
                .extract()
                .as(PlanRs.class);
    }

    public static io.restassured.response.Response getPlan(String projectCode, Integer planId) {
        return given()
                .spec(spec)
                .when()
                .log().all()
                .get("/plan/" + projectCode + "/" + planId)
                .then()
                .log().all()
                .spec(ok200)
                .extract()
                .response();
    }

    public static io.restassured.response.Response deletePlan(String projectCode, Integer planId) {
        return given()
                .spec(spec)
                .when()
                .log().all()
                .delete("/plan/" + projectCode + "/" + planId)
                .then()
                .log().all()
                .spec(ok200)
                .extract()
                .response();
    }
}