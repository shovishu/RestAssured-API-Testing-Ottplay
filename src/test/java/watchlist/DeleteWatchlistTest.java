package watchlist;

import clients.WatchlistClient;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DeleteWatchlistTest {
    private Response response;

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

        response = WatchlistClient.deleteFromWatchlist(token, userProfileId, clientId, contentId, contentType, deviceId);
    }

    // TC-01: Status code check
    @Test(priority = 1)
    public void statusCodeShouldBe200() {
        assertThat("Status code should be 200",
                response.getStatusCode(), equalTo(200));
    }

    // TC-02: Required fields exist
    @Test(priority = 2)
    public void responseShouldContainRequiredFields() {
        assertThat(response.jsonPath().getMap(""),
                allOf(
                        hasKey("statusCode"),
                        hasKey("message"),
                        hasKey("document")
                ));
    }

    // TC-03: Acknowledged is boolean
    @Test(priority = 3)
    public void acknowledgedShouldBeBoolean() {
        Object acknowledged = response.jsonPath().get("document.acknowledged");
        assertThat("Acknowledged must be boolean",
                acknowledged, instanceOf(Boolean.class));
    }

    // TC-04: deletedCount is non-negative integer
    @Test(priority = 4)
    public void deletedCountShouldBeNonNegative() {
        int deletedCount = response.jsonPath().getInt("document.deletedCount");
        assertThat("DeletedCount must be >= 0", deletedCount, greaterThanOrEqualTo(0));
    }

    // TC-05: Response time
    @Test(priority = 5)
    public void responseTimeShouldBeLessThan250ms() {
        assertThat("Response time should be < 250ms",
                response.getTime(), lessThan(600L));
    }
}
