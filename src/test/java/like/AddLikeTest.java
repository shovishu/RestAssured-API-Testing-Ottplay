package like;

import baseClass.BaseClass;
import clients.LikeClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AddLikeTest extends BaseClass {
    private static Response response;
    private Map<String, Object> data;

    @BeforeTest()
    public void setUp() {
        // Always fetch from SessionManager
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String userProfileId = SessionManager.getProfileId();

//        System.out.println("Token: " + token);
//        System.out.println("ClientId: " + clientId);
//        System.out.println("User Profile ID: " + userProfileId);

        response = LikeClient.addToLikes(token, userProfileId);
        data = response.jsonPath().getMap("data");
    }
    // TC-01: Status code is 201
    @Test(priority = 1)
    public void validateStatusCode() {
        assertThat("Status code should be 201", response.getStatusCode(), equalTo(201));
    }

    // TC-02: Message is "Data saved successfully"
    @Test(priority = 2)
    public void validateMessageResponse() {
        String message = response.jsonPath().getString("message");
        assertThat("Message should be 'Data saved successfully'", message, equalTo("Data saved successfully"));
    }

    // TC-03: Data object exists and is not null
    @Test(priority = 3)
    public void validateNoNullData() {
        assertThat("Data object should exist", data, notNullValue());
    }

    // TC-04: Mandatory fields exist and are not null/empty
    @Test(priority = 4)
    public void validateMandatoryFields() {
        assertThat("Data object should exist", data, notNullValue());
        String[] allKeys = {"ottplay_id", "ht_sso_id", "device_id", "movie_pref", "content_type",
                "user_profile_id", "status", "_id", "created_on", "modified_on", "__v"};
        for (String key : allKeys) {
            assertThat("Data should contain key: " + key, data.containsKey(key), is(true));
        }
    }

    // TC-05: Response Time
    @Test(priority = 5)
    public void validateResponseTime() {
        Assert.assertTrue(response.getTime() < 500L,"Response time should be < 500ms");
    }

}
