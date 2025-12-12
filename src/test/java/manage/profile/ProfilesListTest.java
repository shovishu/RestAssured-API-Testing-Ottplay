package manage.profile;

import clients.ManageProfileClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.util.List;
import java.util.Map;

public class ProfilesListTest {

    private static Response response;

    @BeforeClass
    public static void setUp() {
        String randomId = "1234";

        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String userProfileId = SessionManager.getProfileId();

        System.out.println("Token: " + token);
        System.out.println("ClientId: " + clientId);
        System.out.println("User Profile ID: " + userProfileId);

        response = ManageProfileClient.getProfilesList(token,clientId,randomId);
    }

    @Test(priority = 1)
    public static void verifyStatusCode() {
        Assert.assertEquals(response.getStatusCode(), 200, "Invalid Status Code");
    }

    @Test(priority = 2)
    public static void verifyResponseStatus() {
        boolean status = response.jsonPath().getBoolean("status");
        Assert.assertTrue(status, "Status is not true");
    }

    @Test(priority = 2)
    public static void verifyMaxProfilesAllowed() {
        int profiles = response.jsonPath().getInt("maxProfilesAllowed");
        Assert.assertEquals(profiles,4, "maxProfilesAllowed is not 4");
    }

    @Test(priority = 3)
    public static void verifyResponseMessage() {
        String message = response.jsonPath().getString("message");
        Assert.assertEquals(message, "Success","Not Successful");
    }

    @Test(priority = 4)
    public static void verifyDataIsNotNullAndContainsIdAndAvatarUrl() {

// Extract the "data" array
        List<Map<String, Object>> profiles = response.jsonPath().getList("data");

        // Ensure data is not null or empty
        Assert.assertNotNull(profiles, "Data list is null");
        Assert.assertFalse(profiles.isEmpty(), "Data list is empty");

        for (int i = 0; i < profiles.size(); i++) {
            Map<String, Object> profile = profiles.get(i);

            String prefix = "Profile index " + i + " - ";

            // Mandatory fields check
            Assert.assertNotNull(profile.get("user_profile_id"), prefix + "user_profile_id is missing");
            Assert.assertFalse(profile.get("user_profile_id").toString().isEmpty(), prefix + "user_profile_id is empty");

            Assert.assertNotNull(profile.get("user_id"), prefix + "user_id is missing");
            Assert.assertFalse(profile.get("user_id").toString().isEmpty(), prefix + "user_id is empty");

            Assert.assertNotNull(profile.get("name"), prefix + "name is missing");
            Assert.assertFalse(profile.get("name").toString().isEmpty(), prefix + "name is empty");

            Assert.assertNotNull(profile.get("profile_type_key"), prefix + "profile_type_key is missing");
            Assert.assertFalse(profile.get("profile_type_key").toString().isEmpty(), prefix + "profile_type_key is empty");

            Assert.assertNotNull(profile.get("is_profile_pin_created"), prefix + "is_profile_pin_created is missing");

            Assert.assertNotNull(profile.get("is_profile_locked"), prefix + "is_profile_locked is missing");

            // Validate nested object: profile_avatar_id
            Map<String, Object> avatar = (Map<String, Object>) profile.get("profile_avatar_id");
            Assert.assertNotNull(avatar, prefix + "profile_avatar_id is missing");

            Assert.assertNotNull(avatar.get("_id"), prefix + "profile_avatar_id._id is missing");
            Assert.assertFalse(avatar.get("_id").toString().isEmpty(), prefix + "profile_avatar_id._id is empty");

            Assert.assertNotNull(avatar.get("avatar_url"), prefix + "profile_avatar_id.avatar_url is missing");
            Assert.assertFalse(avatar.get("avatar_url").toString().isEmpty(), prefix + "profile_avatar_id.avatar_url is empty");
        }
    }
}
