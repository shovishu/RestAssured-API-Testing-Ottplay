package clients;

import baseClass.BaseClass;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.WatchlistRequest;

import static io.restassured.RestAssured.given;

public class WatchlistClient extends BaseClass {

    //Get the watchlist for the given user profile
    public static Response getWatchlist(String token, String userProfileId, String clientId) {
        Response response = given()
                .spec(authWithProfileSpec(token,userProfileId,clientId))
                .when()
                .get("/api/user-service/v1/user/watchlist/list");
        return response;
    }

    //Add an item to the watchlist for the given user profile
    public static Response addToWatchlist(String token, String userProfileId, String clientId, String contentId, String contentType, String deviceId) {
        WatchlistRequest request = WatchlistRequest.builder()
                .movie_pref(contentId)
                .content_type(contentType)
                .build();

        return given()
                .spec(authWithProfileSpec(token,userProfileId,clientId))
                .header("device_id", deviceId)
                .body(request)
                .when()
                .post("/api/user-service/v1/user/watchlist/add");
    }

    //Delete an item from the watchlist for the given user profile
    public static Response deleteFromWatchlist(String token, String userProfileId, String clientId, String contentId, String contentType, String deviceId) {
        return given()
                .spec(authWithProfileSpec(token,userProfileId,clientId))
                .header("device_id", deviceId)
                .pathParam("contentId", contentId)
                .queryParam("contentType", contentType)
                .when()
                .delete("/api/user-service/v1/user/watchlist/remove/{contentId}");
    }

}
