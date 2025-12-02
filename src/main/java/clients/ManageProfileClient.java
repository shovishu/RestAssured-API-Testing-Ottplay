package clients;

import baseClass.BaseClass;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.CreateProfileRequest;

import static io.restassured.RestAssured.given;

public class ManageProfileClient extends BaseClass {

    public static Response getAvatarList(){
        return given()
                .baseUri(getBaseUrl())
                .header("randomId","1234")
                .accept(ContentType.JSON)
                .when()
                .get("/api/user-service/v1/user/profile/list-avatar")
                .then()
                .extract().response();
    }

    public static Response getProfilesList(String authToken, String clientId){
        return given()
                .baseUri(getBaseUrl())
                .header("randomId","1234")
                .header("authorization","Bearer " + authToken)
                .header("client_id",clientId)
                .accept(ContentType.JSON)
                .when()
                .get("/api/user-service/v1/user/profile/list-profiles")
                .then()
                .extract().response();
    }

    public static Response createNewProfile(String authToken, String clientId){

        CreateProfileRequest request = CreateProfileRequest.builder()
                .avatar_id("65e58d1e32d0991b17bfc043")
                .name("RestAssured Profile")  // Optional: match curl value
                .profile_type(0)
                .profile_type_key("adult_all")
                .build();

        return given()
                .baseUri(getBaseUrl()) // Make sure returns https://api2.ottplay.com
                .header("auth", authToken)
                .header("client_id", clientId)
                .header("apiversion", "1")                    // <-- ADD THIS HEADER
                .contentType(ContentType.JSON)                // <-- SET content type for body
                .accept(ContentType.JSON)                      // Accept JSON response
                .body(request)
                .when()
                .post("/api/user-service/v1/user/profile/create-profile")
                .then()
                .extract()
                .response();
    }

    public static Response deleteProfile(String authToken, String profileId){;

        return given()
                .baseUri(getBaseUrl()) // Make sure returns https://api2.ottplay.com
                .header("authorization","Bearer " + authToken)
                .header("profile_id", profileId)                 // <-- ADD THIS HEADER
                .contentType(ContentType.JSON)                // <-- SET content type for body
                .accept(ContentType.JSON)               // <-- SET content type for body                 // Accept JSON response
                .when()
                .delete("/api/user-service/v1/user/profile/delete-profile")
                .then()
                .extract()
                .response();
    }

}
