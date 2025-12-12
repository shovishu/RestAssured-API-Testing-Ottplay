package clients;

import baseClass.BaseClass;
import io.restassured.response.Response;
import models.CreateProfileRequest;

import static io.restassured.RestAssured.given;

public class ManageProfileClient extends BaseClass {

    public static Response getAvatarList(String randomId) {
        return given()
                .spec(unauthSpec())
                .header("randomId", "1234")
                .when()
                .get("/api/user-service/v1/user/profile/list-avatar")
                .then()
                .extract().response();
    }

    public static Response getProfilesList(String authToken, String clientId, String randomId) {
        return given()
                .spec(authWithClientSpec(authToken, clientId))
                .when()
                .get("/api/user-service/v1/user/profile/list-profiles")
                .then()
                .extract().response();
    }

    public static Response createNewProfile(String authToken, String clientId, int profileType, String profileTypeKey, String avatarId, String profileName) {

        CreateProfileRequest request = CreateProfileRequest.builder()
                .avatar_id(avatarId)
                .name(profileName)
                .profile_type(profileType)
                .profile_type_key(profileTypeKey)
                .build();

        return given()
                .spec(authWithClientSpec(authToken, clientId))
                .header("apiversion", "1")
                .body(request)
                .when()
                .post("/api/user-service/v1/user/profile/create-profile")
                .then()
                .extract()
                .response();
    }

    public static Response deleteProfile(String authToken, String profileId, String clientId) {

        return given()
                .spec(authWithProfileSpec(authToken, profileId, clientId))
                .when()
                .delete("/api/user-service/v1/user/profile/delete-profile")
                .then()
                .extract()
                .response();
    }

}
