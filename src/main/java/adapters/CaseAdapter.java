package adapters;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import models.cases.CaseRq;
import models.cases.CaseRs;

import static io.restassured.RestAssured.given;

@Log4j2
public class CaseAdapter extends BaseAdapter {

    @Step("API: Создание тест-кейса '{caseRq.title}' в проекте '{projectCode}'")
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

    @Step("API: Получение данных тест-кейса ID {caseId} из проекта '{projectCode}'")
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

    @Step("API: Обновление тест-кейса ID {caseId} в проекте '{projectCode}'")
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

    @Step("API: Удаление тест-кейса ID {caseId} из проекта '{projectCode}'")
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
