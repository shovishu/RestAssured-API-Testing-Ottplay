package like;

import baseClass.BaseClass;
import clients.LikeClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Like Module")
@Feature("Add to Likes API")
@Severity(SeverityLevel.CRITICAL)
public class AddLikeTest extends BaseClass {

    private static Response response;
    private Map<String, Object> data;

    @Attachment(value = "API Response", type = "application/json")
    public String attachResponse(String res) {
        return res;
    }

    @Attachment(value = "API Request", type = "text/plain")
    public String attachRequest(String req) {
        return req;
    }

    @BeforeClass
    public void setUp() {
        Allure.step("Fetching session details");

        String contentId = "62f363d2d48670001c502201";
        String contentType = "movie";
        String deviceId = "078104a7928a0908";

        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String userProfileId = SessionManager.getProfileId();

        Allure.step("Sending Add Like API request");

        response = LikeClient.addToLikes(
                token,
                userProfileId,
                clientId,
                contentId,
                contentType,
                deviceId
        );

        attachRequest(
                "token=" + token +
                        "\nclientId=" + clientId +
                        "\nprofileId=" + userProfileId
        );

        attachResponse(response.asString());
        data = response.jsonPath().getMap("data");
    }

    @Test(priority = 1)
    @Description("Validate API response status code")
    public void validateStatusCode() {
        Allure.step("Validating status code 201");
        assertThat(response.getStatusCode(), equalTo(201));
    }

    @Test(priority = 2)
    @Description("Validate message returned in response")
    public void validateMessageResponse() {
        Allure.step("Extracting message");
        String message = response.jsonPath().getString("message");
        assertThat(message, equalTo("Data saved successfully"));
    }

    @Test(priority = 3)
    @Description("Validate data object is not null")
    public void validateNoNullData() {
        Allure.step("Checking data object");
        assertThat(data, notNullValue());
    }

    @Test(priority = 4)
    @Description("Validate mandatory fields inside 'data'")
    public void validateMandatoryFields() {
        Allure.step("Validating mandatory keys inside data");
        String[] allKeys = {
                "ottplay_id", "ht_sso_id", "device_id", "movie_pref", "content_type",
                "user_profile_id", "status", "_id", "created_on", "modified_on", "__v"
        };

        for (String key : allKeys) {
            assertThat("Missing key: " + key, data.containsKey(key), is(true));
        }
    }

    @Test(priority = 5)
    @Description("Validate response time (<500ms)")
    public void validateResponseTime() {
        Allure.step("Checking response time");
        Assert.assertTrue(response.getTime() < 500L, "Response time should be < 500ms");
    }
}
