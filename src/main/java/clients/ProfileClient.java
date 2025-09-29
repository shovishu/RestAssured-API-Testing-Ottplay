package clients;

import baseClass.BaseClass;
import config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;  // <-- ADD THIS IMPORT

import static io.restassured.RestAssured.given;

public class ProfileClient extends BaseClass {

    public static Response getUserProfiles(String authToken, String clientId) {

        return given()
                .spec(new RequestSpecBuilder()
                        .setBaseUri(getBaseUrl())
                        .setContentType(ContentType.JSON)
                        .addHeader("authorization", "Bearer " + authToken)
                        .addHeader("client_id", clientId)
                        .addHeader("randomId", "1234")
                        .build())
                .when()
                .get("/api/user-service/v1/user/profile/list-profiles").then()
                .extract()
                .response();
    }
}
