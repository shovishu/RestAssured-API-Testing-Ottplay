package clients;

import baseClass.BaseClass;
import config.ConfigManager;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class VideoPlayerClient extends BaseClass {

    public static Response getVideoPlayer(String authToken) {
        return given()
                .baseUri(getBaseUrl())
                .header("auth", authToken)
                .header("device_id", "831eed07-1dd8-4710-8311-c45c3c0ed86c")
                .header("device_type", "web")
                .contentType(ContentType.JSON)
                .queryParam("ottplay_id", "23b3457c1d8da")
                .queryParam("provider", "60abaef7b17a77001df4b8fa")
                .queryParam("content_type", "movie")
                .when()
                .get("/api/partner-service/v4.2/videoplayer")
                .then()
                .extract().response();
    }
}
