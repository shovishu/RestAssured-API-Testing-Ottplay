package whatsAppConsent;

import baseClass.BaseClass;
import clients.WhatsAppConsentClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

public class SubmitWhatsAppConsentTests extends BaseClass {
    private static Response response;

    @BeforeClass()
    public void setUp() {
        String token = SessionManager.getAuthToken();
        response = WhatsAppConsentClient.submitWhatsAppConsent(token);
    }

    @Test(priority = 1)
    public static void validateStatusCode(){
        Assert.assertEquals(response.getStatusCode(),200,"Status code mismatch!");
    }

    @Test(priority = 2)
    public static void validateResponseBody() {
        String phone = response.jsonPath().getString("phone");
        Assert.assertNotNull(phone, "whatsAppNotification should not be null!");
    }

    @Test(priority = 2)
    public static void validateStatusSuccess() {
        String phone = response.jsonPath().getString("status");
        Assert.assertEquals(phone, "success");
    }

}
