package adapters;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import models.suite.SuiteRq;
import models.suite.SuiteRs;
import static io.restassured.RestAssured.given;

@Log4j2
public class SuiteAdapter extends BaseAdapter {

    @Step("API: Создание тест-сьюта '{suiteRq.title}' в проекте '{projectCode}'")
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

    @Step("API: Получение структуры тест-сьюта ID {suiteId} из проекта '{projectCode}'")
    public static Response getSuite(String projectCode, Integer suiteId) {
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

    @Step("API: Удаление тест-сьюта ID {suiteId} из проекта '{projectCode}'")
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
