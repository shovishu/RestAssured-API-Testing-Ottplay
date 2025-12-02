package ManageProfiles;

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
        String newProfileId = SessionManager.getNewProfileId();
        response = ManageProfileClient.deleteProfile(token,newProfileId);
        response.prettyPrint();
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
        long responseTime = response.getTime();
        Assert.assertEquals(responseTime,500L,"Took Longer than expected");
    }
}
