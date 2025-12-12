package clients;

import baseClass.BaseClass;
import config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;  // <-- ADD THIS IMPORT

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProfileClient extends BaseClass {

    public static Response getUserProfiles(String authToken, String clientId) {

        // Used map beacuse clientID and authToken is dynamic whihc we'll get from the response of login API
        Map<String, String> headers = new HashMap<>();
        headers.put("randomId", "1234");

        return given()
                .spec(authWithClientSpec(authToken,clientId))
                .headers(headers)
                .when()
                .get("/api/user-service/v1/user/profile/list-profiles")
                .then()
                .extract()
                .response();
    }
}
