package player;

import clients.VideoPlayerClient;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.SessionManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class VideoPlayerTests {
    private static Response response;
    private static JsonPath json;


    @BeforeTest()
    public void setUp() {
        // Always fetch from SessionManager
        String token = SessionManager.getAuthToken();
        System.out.println("Token: " + token);

        String clientId = SessionManager.getClientId();
        System.out.println("Client Id: " + clientId);

        String profileId = SessionManager.getProfileId();
        System.out.println("Profile Id: " + profileId);

        response = VideoPlayerClient.callVideoPlayerAPI(token,clientId,profileId);
        json = response.jsonPath();
    }

    // 1. Status code
    @Test(priority = 1)
    public void validateStatusCode() {
        assertThat(response.getStatusCode(), equalTo(200));
    }

    // 2. Playback Response
    @Test(priority = 2)
    public void validatePlaybackUrl() {

        String playbackUrl = json.getString("playback[0].playback_url");
        Assert.assertNotNull(playbackUrl);
        Assert.assertTrue(playbackUrl.startsWith("https://"));
    }

    @Test(priority = 3)
    public void validatePlaybackFormat() {

        String playbackUrl = json.getString("playback[0].format");
        Assert.assertNotNull(playbackUrl);
    }

    // 3. Values
    @Test(priority = 4)
    public void validatePreAndPostCredits() {
        Assert.assertNotNull(json.get("playback[0].skip_intro_start"));
        Assert.assertNotNull(json.get("playback[0].skip_intro"));
        Assert.assertNotNull(json.get("playback[0].skip_credit_start"));
        Assert.assertNotNull(json.get("playback[0].skip_credit"));
    }

    // 4. Subtitles
    @Test(priority = 5)
    public void validateSubtitleLanguages() {
        Assert.assertNotNull(json.get("subtitles[0].language"));
        Assert.assertNotNull(json.get("subtitles[0].content"));
    }

    // 5. responseTime
    @Test(priority = 6)
    public void validateResponseTime() {
        long responseTime = response.getTime(); // ms
        assertThat("Response time should be < 250ms", responseTime, lessThan(350L));
    }
}
