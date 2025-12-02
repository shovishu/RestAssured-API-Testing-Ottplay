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

        response = WatchlistClient.getWatchlist(token,profileId);
        response.prettyPrint();
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
    public void validateResultsAndIds() {
        // ✅ Step 1: Ensure the 'result' key actually exists
        Object resultObj = json.get("result");
        Assert.assertNotNull(resultObj, "❌ 'result' key is missing or null in response");

        // ✅ Step 2: Get list safely
        List<Map<String, Object>> results = json.getList("result");
        Assert.assertNotNull(results, "❌ 'result' array not found or is null");
        Assert.assertTrue(results.size() > 0, "❌ 'result' array is empty");

        System.out.println("✅ Total results: " + results.size());

        // ✅ Step 3: Validate each '_id' inside result array
        for (int i = 0; i < results.size(); i++) {
            Map<String, Object> item = results.get(i);

            Assert.assertTrue(item.containsKey("_id"), "❌ Missing '_id' in result[" + i + "]");
            Assert.assertNotNull(item.get("_id"), "❌ '_id' is null in result[" + i + "]");
            Assert.assertFalse(item.get("_id").toString().trim().isEmpty(),
                    "❌ '_id' is empty in result[" + i + "]");

            System.out.println("✅ result[" + i + "] _id = " + item.get("_id"));
        }
    }



    @Test(priority = 6)
    public void validateResponseTime(){
        Assert.assertTrue(response.getTime() <= 2000, "Response time exceeded 2000ms");
    }
}
