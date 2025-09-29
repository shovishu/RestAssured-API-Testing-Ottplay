package whatsAppConsent;

import baseClass.BaseClass;
import clients.WatchlistClient;
import clients.WhatsAppConsentClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetWhatsAppConsentStatusTests extends BaseClass {
    private static Response response;

    @BeforeClass()
    public void setUp() {
        String token = SessionManager.getAuthToken();
        response = WhatsAppConsentClient.getWhatsAppConsentStatus(token);
    }

    @Test(priority = 1)
    public static void validateStausCode() {
        assertThat(response.getStatusCode(), equalTo(200));
    }

    @Test(priority = 2)
    public void validateStatusField() {
        String status = response.jsonPath().getString("status");
        Assert.assertEquals(status, "success", "Status field does not match!");
    }

    @Test(priority = 3)
    public void validateWhatsAppNotification() {
        boolean whatsAppNotification = response.jsonPath().getBoolean("whatsAppNotification");
        Assert.assertTrue(whatsAppNotification, "WhatsApp Notification should be true but it's false!");
    }

}
