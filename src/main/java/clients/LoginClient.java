package clients;

import baseClass.BaseClass;
import io.restassured.response.Response;
import models.LoginRequest;
import static io.restassured.RestAssured.given;

public class LoginClient extends BaseClass {

    public static Response login() {

        LoginRequest request = LoginRequest.builder()
                .cellNumber(getCellNumber())
                .password(getPassword())
                .referrer("OTT")
                .newsletterConsent(true)
                .build();

        return given()
                .spec(unauthSpec())
                .body(request)
                .when()
                .post("/api/user-service/v1/verify-via-password")
                .then()
                .extract().response();

    }
}
