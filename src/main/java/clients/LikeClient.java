package clients;

import baseClass.BaseClass;
import io.restassured.response.Response;
import models.LikeRequest;

import static io.restassured.RestAssured.given;

public class LikeClient extends BaseClass {

    //Get the Like List for the given user profile
    public static Response getLikes(String token, String userProfileId, String clientId) {
        return given()
                .spec(authWithProfileSpec(token, userProfileId, clientId))
                .when()
                .get("/api/user-service/v1/user/like/list");
    }

    //Add an item to the watchlist for the given user profile
    public static Response addToLikes(String token, String userProfileId, String clientId, String contentId, String contentType, String deviceId) {
        LikeRequest request = LikeRequest.builder()
                .movie_pref(contentId)
                .content_type(contentType)
                .build();

        return given()
                .spec(authWithProfileSpec(token, userProfileId, clientId))
                .header("device_id", deviceId)
                .body(request)
                .when()
                .post("/api/user-service/v1/user/like/add");
    }

    //Delete an item from Like for the given user profile
    public static Response deleteFromLikes(String token, String userProfileId, String clientId, String contentType, String contentId) {

        return given()
                .spec(authWithProfileSpec(token, userProfileId, clientId))
                .pathParam("contentId", contentId)
                .queryParam("contentType", contentType)
                .when()
                .delete("/api/user-service/v1/user/like/remove/{contentId}");
    }

}
