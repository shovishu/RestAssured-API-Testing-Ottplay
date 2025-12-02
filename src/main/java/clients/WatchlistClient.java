package clients;

import baseClass.BaseClass;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.WatchlistRequest;

import static io.restassured.RestAssured.given;

public class WatchlistClient extends BaseClass {
    /**
     * Get the watchlist for the given user profile
     */
    public static Response getWatchlist(String token, String userProfileId) {
        Response response = given()
                .baseUri(getBaseUrl())
                .header("Authorization", "Bearer " + token)
                .header("profile_id", userProfileId)
                .header("accept", "application/json")
                .when()
                .get("/api/user-service/v1/user/watchlist/list"); // replace with actual endpoint

        // Print full response to console
//        response.prettyPrint();

        return response;
    }


    /**
     * Add an item to the watchlist for the given user profile
     */
    public static Response addToWatchlist(String token) {
        WatchlistRequest request = WatchlistRequest.builder()
                .movie_pref("6867af9c9d3cca00291f5816")
                .content_type("movie")
                .build();

        return given()
                .baseUri(getBaseUrl())
                .header("auth", token)
                .header("accept", "application/json")
                .header("device_id", "99f019da-df25-4d12-9720-4abc0b6a30e9")
//                .header("profile_id", userProfileId)
//                .header("client_id", clientId)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/user-service/v1/user/watchlist/add"); // replace with actual endpoint
    }

    /**
     * Delete an item from the watchlist for the given user profile
     */
    public static Response deleteFromWatchlist(String token, String userProfileId) {
        return given()
                .baseUri(getBaseUrl())
                .header("auth", token)
                .header("profile_id", userProfileId)
                .header("accept", "application/json")
                .contentType(ContentType.JSON)
                .queryParam("contentId", "6867af9c9d3cca00291f5816")
                .queryParam("contentType", "show")
                .when()
                .delete("/api/user-service/v1/user/watchlist/remove");
    }

}
