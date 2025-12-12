package manage.profile;

import clients.ManageProfileClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import static org.testng.Assert.assertNotNull;

public class CreateProfileTests {
    private static Response response;

    @BeforeClass
    public static void setUp() {

        int profileType = 0;
        String profileTypeKey = "adult_all";
        String avatarId = "65e58d1e32d0991b17bfc043";
        String profileName = "RestAssured Profile";

        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getAuthToken();
        String userProfileId = SessionManager.getProfileId();

        System.out.println("Token: " + token);
        System.out.println("ClientId: " + clientId);
        System.out.println("User Profile ID: " + userProfileId);

        response = ManageProfileClient.createNewProfile(token,clientId,profileType,profileTypeKey,avatarId,profileName);
        String profileId = response.jsonPath().getString("data.user_profile_id");
        SessionManager.setCreatedProfileId(profileId);
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

    @Test(priority = 3)
    public static void verifyResponseMessage() {
        String message = response.jsonPath().getString("message");
        Assert.assertEquals(message, "Profile created successfully", "Not Successful");
    }

    @Test(priority = 4)
    public static void verifyMandatoryFiledInResponse() {

        // Verify data fields are not null
        String userProfileId = response.jsonPath().getString("data.user_profile_id");
        String userId = response.jsonPath().getString("data.user_id");
        String name = response.jsonPath().getString("data.name");
        String status = response.jsonPath().getString("data.status");

        Assert.assertNotNull(userProfileId, "user_profile_id should not be null");
        Assert.assertNotNull(userId, "user_id should not be null");
        Assert.assertNotNull(name, "name should not be null");
        Assert.assertNotNull(status, "status should not be null");
    }

    @Test(priority = 5)
    public static void verifyResponseTime() {
        long responseTime = response.getTime(); // ms
        System.out.println(responseTime);
        Assert.assertTrue(responseTime< 5000L,"Response time should be < 250ms");
    }
}
