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
                .queryParam("ottplay_id", "d235f80127803")
                .queryParam("provider", "60abaef7b17a77001df4b8fa")
                .queryParam("randomId", "1875")
                .queryParam("error_version", "2")

                // Headers
                .header("accept", "application/json, text/plain, */*")
                .header("accept-language", "en-US,en;q=0.9,hi;q=0.8")
                .header("apiversion", "1")
                .header("auth", authToken) // dynamic
                .header("authorization", "Bearer " + authToken) // dynamic
                .header("client_id", clientId) // dynamic
                .header("device_id", "a97642ed-f4bf-4449-81f7-cd29cb7adab8") // dynamic
                .header("device_type", "web")
                .header("devicetype", "web")
                .header("dnt", "1")
                .header("origin", "https://www.ottplay.com")
                .header("pc_type", "0")
                .header("plan_code", "ott_all")
                .header("platform", "web")
                .header("priority", "u=1, i")
                .header("profile_id", profileId) // dynamic
                .header("referer", "https://www.ottplay.com/movie/den-of-thieves-2-pantera-2025/d235f80127803")
                .header("sec-ch-ua", "\"Chromium\";v=\"140\", \"Not=A?Brand\";v=\"24\", \"Google Chrome\";v=\"140\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"Windows\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-site")
                .header("source", "web")
                .header("unique_id", "a97642ed-f4bf-4449-81f7-cd29cb7adab8")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36")
                .header("user_id", clientId)

                .contentType(ContentType.JSON)

                .when()
                .get("/api/v3.1/web/mediaprogress");
    }}
