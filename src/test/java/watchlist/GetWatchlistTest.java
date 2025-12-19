package watchlist;

import baseClass.BaseClass;
import clients.MediaProgressClient;
import clients.WatchlistClient;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetWatchlistTest extends BaseClass {

    private Response response;  // class-level field
    private JsonPath json;      // class-level field

    @BeforeClass
    public void setUp() {
        String token = SessionManager.getAuthToken();
        String profileId = SessionManager.getProfileId();
        String clientId = SessionManager.getClientId();
        String userProfileId = SessionManager.getProfileId();

        System.out.println("Token: " + token);
        System.out.println("ClientId: " + clientId);
        System.out.println("User Profile ID: " + userProfileId);

        response = WatchlistClient.getWatchlist(token, userProfileId, clientId);
        json = response.jsonPath();
    }

    // 1️⃣ Validate status code and response time
    @Test(priority = 1)
    public void testStatusCode() {
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
    }

    @Test(priority = 2)
    public void GetProfileWatchlistTest() {
        Assert.assertNotNull(json.get("totalDocuments"), " should not be null");
    }

    @Test(priority = 3)
    public void validateAllMoviePrefAssetIds() {

        List<Map<String, Object>> results = json.getList("result");
        Assert.assertNotNull(results, "❌ 'result' array is null");
        Assert.assertFalse(results.isEmpty(), "❌ 'result' array is empty");

        for (int i = 0; i < results.size(); i++) {

            String assetId = json.getString("result[" + i + "].movie_pref._id");

            Assert.assertNotNull(assetId, "❌ movie_pref._id is null in result[" + i + "]");
            Assert.assertFalse(assetId.trim().isEmpty(),
                    "❌ movie_pref._id is empty in result[" + i + "]");

            System.out.println("🎯 result[" + i + "] movie_pref._id = " + assetId);
        }
    }


    @Test(priority = 4)
    public void validateResponseTime() {
        Assert.assertTrue(response.getTime() <= 2000, "Response time exceeded 2000ms");
    }
}
