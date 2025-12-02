package clients;

import baseClass.BaseClass;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.LikeRequest;

import static io.restassured.RestAssured.given;

public class LikeClient extends BaseClass {
    /**
     * Get the Like List for the given user profile
     */
    public static Response getLikes(String token, String userProfileId, String clientId) {
        return given()
                .baseUri(getBaseUrl())
                .header("Authorization", "Bearer " + token)
                .header("profile_id", userProfileId)
                .header("client_id", clientId)
                .header("accept", "application/json")
                .when()
                .get("/api/user-service/v1/user/like/list"); // replace with actual endpoint
    }

    /**
     * Add an item to the watchlist for the given user profile
     */
    public static Response addToLikes(String token,String userProfileId) {
        LikeRequest request = LikeRequest.builder()
                .movie_pref("6890d226a00e1e002fdc5f62")
                .content_type("movie")
                .build();

        return given()
                .baseUri(getBaseUrl())
                .header("auth", token)
                .header("profile_id", userProfileId)
                .header("accept", "application/json")
                .header("device_id", "667d45f2-bcca-4428-94a5-d41a09d4cb86")
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/user-service/v1/user/like/add"); // replace with actual endpoint
    }

    /**
     * Delete an item from the watchlist for the given user profile
     */
    public static Response deleteFromLikes(String token, String userProfileId) {
        String contentId = "6890d226a00e1e002fdc5f62"; // hardcoded

        return given()
                .baseUri(getBaseUrl())
                .header("auth", token)
                .header("profile_id" , userProfileId)
                .header("accept", "application/json")
                .queryParam("contentType", "movie")
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/user-service/v1/user/like/remove/" + contentId);
    }

}
