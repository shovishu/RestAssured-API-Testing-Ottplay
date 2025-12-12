package manage.profile;

import clients.ManageProfileClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

public class DeleteProfileTests {
    private static Response response;

    @BeforeClass
    public static void setUp() {
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String userProfileId = SessionManager.getProfileId();

        System.out.println("Token: " + token);
        System.out.println("ClientId: " + clientId);
        System.out.println("User Profile ID: " + userProfileId);

        String newProfileId = SessionManager.getCreatedProfileId();
        response = ManageProfileClient.deleteProfile(token,newProfileId,clientId);
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
        Assert.assertEquals(message, "Profile deleted successfully","Not Successful");
    }

    @Test(priority = 4)
    public static void verifyResponseTime(){
        long responseTime = response.getTime(); // ms
        System.out.println(responseTime);
        Assert.assertTrue(responseTime< 5000L,"Response time should be < 250ms");
    }
}
