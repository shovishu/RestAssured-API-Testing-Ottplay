package whatsAppConsent;

import baseClass.BaseClass;
import clients.WhatsAppConsentClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

public class UpdateWhatsAppConsent extends BaseClass {
    private static Response response;

    @BeforeClass()
    public void setUp() {
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        response = WhatsAppConsentClient.updateWhatsAppConsent(token,clientId);
    }

    @Test(priority = 1)
    public static void validateStatusCode(){
        Assert.assertEquals(response.getStatusCode(),200,"Status code mismatch!");
    }

    @Test(priority = 2)
    public static void validateResponseBody() {
        String phone = response.jsonPath().getString("message");
        Assert.assertEquals(phone, "Your request has been accepted and will be processed soon.");
    }

    @Test(priority = 3)
    public static void validateStatusSuccess() {
        String phone = response.jsonPath().getString("status");
        Assert.assertEquals(phone, "success");
    }
}
