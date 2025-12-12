package watchlist;

import baseClass.BaseClass;
import clients.WatchlistClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.util.Map;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AddWatchlistTest extends BaseClass {
    private Response response;
    private Map<String, Object> responseData;

    @BeforeClass
    public void setup() {
        String contentId = "62f363d2d48670001c502201";
        String contentType = "movie";
        String deviceId = "078104a7928a0908";
        // Always fetch from SessionManager
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String userProfileId = SessionManager.getProfileId();

        System.out.println("Token: " + token);
        System.out.println("ClientId: " + clientId);
        System.out.println("User Profile ID: " + userProfileId);

        // Call API
        response = WatchlistClient.addToWatchlist(token, userProfileId, clientId, contentId, contentType, deviceId);
        responseData = response.jsonPath().getMap("data");
    }

    // 1️⃣ Status code validation
    @Test(priority = 1, description = "Verify Status Code")
    public void statusCodeTest() {
        assertThat("Response status code should be 201", response.getStatusCode(), equalTo(201));
    }

    // 2️⃣ Top-level fields validation
    @Test(priority = 2, description = "Verify the Response Message")
    public void topLevelFieldsTest() {
        Map<String, Object> json = response.jsonPath().getMap("");
        assertThat("Response should contain statusCode", json, hasKey("statusCode"));
        assertThat("Response should contain message", json, hasKey("message"));
    }

    // 3️⃣ Response body not null
    @Test(priority = 3, description = "Verify Response data is not null")
    public void responseDataNotNullTest() {
        assertThat("Data object should not be null", responseData, notNullValue());
    }


    // 4️⃣ Required fields inside data
    @Test(priority = 4, description = "Verify Mandatory Fields")
    public void requiredFieldsInDataTest() {
        String[] requiredFields = {
                "ottplay_id", "ht_sso_id", "device_id", "movie_pref",
                "content_type", "user_profile_id", "status", "_id",
                "created_on", "modified_on", "__v"
        };
        for (String field : requiredFields) {
            assertThat("Field '" + field + "' should exist", responseData, hasKey(field));
        }
    }

    // 5️⃣ Non-empty ottplay_id
    @Test(priority = 5, description = "Verify OttplayId is not null")
    public void ottplayIdNotEmptyTest() {
        String ottplayId = (String) responseData.get("ottplay_id");
        assertThat("ottplay_id should not be empty", ottplayId, not(isEmptyString()));
    }

    // 6️⃣ Date fields validation
    @Test(priority = 6, description = "Verified Created and Modified Date")
    public void dateFieldsFormatTest() {
        String createdOn = (String) responseData.get("created_on");
        String modifiedOn = (String) responseData.get("modified_on");

        String isoPattern = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";
        assertThat("created_on should match ISO format", createdOn, matchesPattern(isoPattern));
        assertThat("modified_on should match ISO format", modifiedOn, matchesPattern(isoPattern));
    }

    // 7️⃣ Response time validation
    @Test(priority = 7, description = "Verify Response Time")
    public void responseTimeTest() {
        long responseTime2 = response.time();
        Assert.assertTrue(responseTime2 < 3000, "Response time greater than Expected");
    }

}


