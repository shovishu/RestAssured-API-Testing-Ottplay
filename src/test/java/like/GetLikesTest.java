package like;

import baseClass.BaseClass;
import clients.LikeClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Like Module")
@Feature("Get All Likes API")
@Severity(SeverityLevel.CRITICAL)
public class GetLikesTest extends BaseClass {

    private static Response response;

    @Attachment(value = "API Response", type = "application/json")
    public void attachResponse(String res) {
    }

    @Attachment(value = "API Request", type = "text/plain")
    public void attachRequest(String req) {
    }

    @BeforeClass()
    public void setUp() {
        Allure.step("Fetching session details");

        // Always fetch from SessionManager
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String userProfileId = SessionManager.getProfileId();

        System.out.println("Token: " + token);
        System.out.println("ClientId: " + clientId);
        System.out.println("User Profile ID: " + userProfileId);

        Allure.step("Sending Add Like API request");

        response = LikeClient.getLikes(token, userProfileId, clientId);

        attachRequest(
                "token=" + token +
                        "\nclientId=" + clientId +
                        "\nprofileId=" + userProfileId
        );

        attachResponse(response.asString());
    }

    // TC-01: "Status Code is 200"
    @Test(priority = 1)
    public void GetProfileLikeTest() {
        assertThat("Status code should be 200", response.getStatusCode(), equalTo(200));
    }

    // TC-02: Message is "Data saved successfully"
    @Test(priority = 2)
    public void validateMessageResponse() {
        Integer totalDocuments = response.path("totalDocuments");
        Assert.assertNotNull(totalDocuments, "❌ totalDocuments is null");
//        System.out.println("✅ totalDocuments: " + totalDocuments);
    }

    // TC-03: Mandatory fields exist and are not null/empty
    @Test(priority = 4)
    public void validateMandatoryFields() {
        List<String> ids = response.path("result._id");
        Assert.assertTrue(ids != null && !ids.isEmpty(), "❌ result._id list is null or empty");

        // 5️⃣ Loop through each _id to ensure no nulls
        for (String id : ids) {
            Assert.assertNotNull(id, "❌ Found null _id in result array");
        }
    }

    // TC-04: Response Time
    @Test(priority = 5)
    public void validateResponseTime() {
        Assert.assertTrue(response.getTime() < 500L, "Response time should be < 500ms");
    }


}

