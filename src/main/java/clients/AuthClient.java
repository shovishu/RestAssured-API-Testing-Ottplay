package clients;

import baseClass.BaseClass;
import io.restassured.response.Response;
import models.LoginRequest;
import static io.restassured.RestAssured.given;

public class AuthClient extends BaseClass {

    public static Response login() {

        LoginRequest request = LoginRequest.builder()
                .cellNumber(getCellNumber())
                .password(getPassword())
                .referrer("OTT")
                .newsletterConsent(true)
                .build();

        return given()
                .baseUri(getBaseUrl())
                .header("accept", "application/json")
                .header("x-client", 1003)
                .header("platform", "web")
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/user-service/v1/verify-via-password")
                .then()
                .extract().response();

    }
}
