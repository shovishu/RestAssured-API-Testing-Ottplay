package player;

import clients.MediaProgressClient;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MediaProgressTests {

    private Response response;  // class-level field
    private JsonPath json;      // class-level field

    @BeforeClass
    public void setUp() {
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String profileId = SessionManager.getProfileId();
        System.out.println("Token: " + token);
        System.out.println("Client Id: " + clientId);
        System.out.println("Profile Id: " + profileId);

        response = MediaProgressClient.getMediaProgress(token, clientId,profileId);
        response.prettyPrint();
        json = response.jsonPath();
    }


    // 1️⃣ Validate status code and response time
    @Test(priority = 1)
    public void testStatusCode() {
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
    }

    // 2️⃣ Playback array and URL validation
    @Test(priority = 2)
    public void testPlaybackUrl() {
        String connectionId = json.get("connection_id");
        String connectionState = json.get("connection_state");

        Assert.assertNotNull(connectionId, "Connect Id should exists");
        Assert.assertNotNull(connectionState, "Connect State should exists");
    }

    // 3️⃣ Values section validation
    @Test(priority = 3)
    public void testValuesSection() {

        Assert.assertNotNull(json.get("progress"), "progress should not be null");
        Assert.assertNotNull(json.get("duration"), "duration should not be null");
    }

    // 4️⃣ Subtitles validation
    @Test(priority = 4)
    public void testSubtitles() {
        Assert.assertNotNull(json.get("_id"), "Media ID should not be null");
        Assert.assertNotNull(json.get("ht_sso_id"), "Ht SSO should not be null");
        Assert.assertNotNull(json.get("ottplay_id"), "Ottplay ID should not be null");
        Assert.assertNotNull(json.get("profile_id"), "Profile ID should not be null");
    }

    @Test(priority = 5)
    public void validateBooleanFlags() {
        // List of boolean fields to validate
        String[] booleanKeys = {
                "disable_continue_watching",
                "disable_recently_view",
                "continue_watching",
                "is_watching",
                "max_devices_reached"
        };

        for (String key : booleanKeys) {
            Object value = json.get(key);

            // 1️⃣ Ensure key exists
            Assert.assertNotNull(value, "❌ Key missing in response: " + key);

            // 2️⃣ Ensure value is of boolean type
            Assert.assertTrue(value instanceof Boolean,
                    "❌ Expected boolean for key: " + key + " but found: " + value.getClass().getSimpleName());

            // 3️⃣ Just print the value for logging/debug
            System.out.println("✅ " + key + " = " + value);
        }
    }

    @Test(priority = 6)
    public void validateResponseTime(){
        Assert.assertTrue(response.getTime() <= 2000, "Response time exceeded 2000ms");
    }

}
