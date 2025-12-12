package like;

import clients.LikeClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.SessionManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Like Module")
@Feature("Delete Likes API")
@Severity(SeverityLevel.CRITICAL)
public class DeleteLikeTest {
    private static Response response;

    @Attachment(value = "API Response", type = "application/json")
    public String attachResponse(String res) {
        return res;
    }

    @Attachment(value = "API Request", type = "text/plain")
    public String attachRequest(String req) {
        return req;
    }

    @BeforeClass()
    public void setUp() {
        String contentId = "62f363d2d48670001c502201";
        String contentType = "movie";

        // Always fetch from SessionManager
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String userProfileId = SessionManager.getProfileId();

        System.out.println("Token: " + token);
        System.out.println("ClientId: " + clientId);
        System.out.println("User Profile ID: " + userProfileId);

        response = LikeClient.deleteFromLikes(token, userProfileId, clientId, contentType, contentId);

        attachRequest(
                "token=" + token +
                        "\nclientId=" + clientId +
                        "\nprofileId=" + userProfileId
        );

        attachResponse(response.asString());
    }

    @Test(priority = 1)
    public void validateStatusCode() {
        assertThat(response.getStatusCode(), equalTo(200));
    }

    @Test(priority = 2)
    public void validateMessageResponse() {
        String message = response.jsonPath().getString("message");
        assertThat("Message should be 'Data saved successfully'", message, equalTo("data deleted successfully"));
    }

    @Test(priority = 3)
    public void validateDataSection() {
        int dataStatus = response.path("data.status");
        String dataMessage = response.jsonPath().getString("data.message");

        Assert.assertEquals(dataStatus, 200, "❌ data.status is not 200");
        Assert.assertEquals(dataMessage, "success", "❌ data.message is incorrect");
    }

    @Test(priority = 4)
    public void validateDocumentSection() {
        Boolean acknowledged = response.path("document.acknowledged");
        Integer deletedCount = response.path("document.deletedCount");

        Assert.assertTrue(acknowledged, "❌ document.acknowledged is false");
        Assert.assertEquals(deletedCount.intValue(), 1, "❌ deletedCount is not 1");
    }

    @Test(priority = 5)
    public void validateResponseTime() {
        Assert.assertTrue(response.getTime() < 500L, "Response time should be < 500ms");
    }
}


