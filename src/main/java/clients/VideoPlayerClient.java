package clients;

import baseClass.BaseClass;
import config.ConfigManager;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class VideoPlayerClient extends BaseClass {

    public static Response callVideoPlayerAPI(String authToken, String clientId, String profileId) {

        return given()
                .baseUri(getBaseUrl())

                .header("auth", authToken)
                .header("authorization", "Bearer " + authToken)
                .header("client_id", clientId)
                .header("content_type", "show")
                .header("device_id", "d7582b96-240e-43f6-b545-9ccd0924ecee")
                .header("device_type", "web")
                .header("hash", "65346634386666336536383633356634")
                .header("ottplay_id", "0bd65f1268101")
                .header("profile_id", profileId)
                .header("provider", "5f456c2aff9ccd034434e6fd")
                .header("unique_id", "d7582b96-240e-43f6-b545-9ccd0924ecee")
                .header("user_id", clientId)

                .queryParam("ottplay_id", "0bd65f1268101")
                .queryParam("provider", "5f456c2aff9ccd034434e6fd")
                .queryParam("content_type", "show")
                .queryParam("hash", "65346634386666336536383633356634")

                .when()
                .get("/api/partner-service/v4.2/videoplayer")
                .then()
                .extract()
                .response();
    }

}
