package clients;

import baseClass.BaseClass;
import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class MediaProgressClient extends BaseClass {

    public static Response getMediaProgress(String authToken, String clientId, String profileId) {
        return RestAssured.given()
                .baseUri(getBaseUrl())

                // Query parameters
                .queryParam("ottplay_id", "d4215b52ed607")
                .queryParam("provider", "665f05ca565609001dee29b3")
                .queryParam("randomId", "361")

                // Headers
                .header("auth", authToken) // dynamic
                .header("authorization", "Bearer F421D63D166CA343454DD833B599C" ) // dynamic
                .header("client_id", clientId) // dynamic
                .header("profile_id", profileId) // dynamic
                .header("user_id", clientId)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v3.1/web/mediaprogress");
    }}
