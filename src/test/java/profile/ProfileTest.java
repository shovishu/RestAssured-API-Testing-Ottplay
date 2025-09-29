package profile;

import baseClass.BaseClass;
import clients.AuthClient;
import clients.ProfileClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProfileTest extends BaseClass {
    private static Response response;

    @BeforeClass
    public void setup() {
        // Always fetch from SessionManager
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();

        System.out.println("Token: " + token);
        System.out.println("ClientId: " + clientId);

        // Pass the valid token
        response = ProfileClient.getUserProfiles(token, clientId);
        response.prettyPrint();

    }


    @Test(priority = 1,description = "Validate Top Level Response from Th Body")
    public void validateTopLevelResponse() {
        Assert.assertTrue(response.jsonPath().getBoolean("status"), "Status should be true");
        Assert.assertEquals(response.jsonPath().getInt("statusCode"), 200, "Status code mismatch");
        Assert.assertEquals(response.jsonPath().getString("message"), "Success", "Message mismatch");
        Assert.assertTrue(response.jsonPath().getInt("maxProfilesAllowed") > 0, "Max profiles allowed should be > 0");
    }

    @Test(priority = 2,description = "Validate User Profile Details")
    public void validateUserProfiles() {
        List<Map<String, Object>> profiles = response.jsonPath().getList("data");

        Assert.assertFalse(profiles.isEmpty(), "Profiles list should not be empty");

        for (Map<String, Object> profile : profiles) {
            Assert.assertNotNull(profile.get("user_profile_id"), "User profile ID should not be null");
            Assert.assertNotNull(profile.get("user_id"), "User ID should not be null");
            Assert.assertNotNull(profile.get("name"), "Profile name should not be null");
            Assert.assertEquals(profile.get("status"), "active", "Profile status should be active");
            Assert.assertNotNull(profile.get("profile_type_key"), "Profile type key should not be null");
            Assert.assertTrue(profile.get("is_profile_pin_created") instanceof Boolean, "Pin flag should be boolean");

            Map<String, String> avatar = (Map<String, String>) profile.get("profile_avatar_id");
            Assert.assertNotNull(avatar.get("_id"), "Avatar ID should not be null");
            Assert.assertNotNull(avatar.get("avatar_url"), "Avatar URL should not be null");

            // Arrays
            Assert.assertTrue(profile.get("show_likes") instanceof List, "show_likes should be a list");
            Assert.assertTrue(profile.get("genre_pref") instanceof List, "genre_pref should be a list");
        }
    }

    @Test(priority = 3,description = "Validate Device Preferences")
    public void validateDevicePreferences() {
        Map<String, Boolean> devicePref = response.jsonPath().getMap("devicePreference");

        Assert.assertTrue(devicePref.get("autoplay_title_flag"), "autoplay_title_flag should be true");
        Assert.assertTrue(devicePref.get("autoplay_trailer_flag"), "autoplay_trailer_flag should be true");
        Assert.assertTrue(devicePref.get("live_tv_flag"), "live_tv_flag should be true");
        Assert.assertTrue(devicePref.get("whats_new_flag"), "whats_new_flag should be true");
    }
    @Test(priority = 4,description = "Validate API response time is within acceptable limit")
    public void validateResponseTime() {
        // Get response time in milliseconds
        long responseTime = response.getTime();
        System.out.println("Response time: " + responseTime + " ms");
        // Assert response time < 2000 ms (2 seconds)
        Assert.assertTrue(responseTime < 2000, "Response time is too high!");    }
}
