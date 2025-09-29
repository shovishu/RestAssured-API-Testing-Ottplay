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

        response = VideoPlayerClient.getVideoPlayer(token);
        json = response.jsonPath();
    }

    // 1. Status code
    @Test(priority = 1)
    public void validateStatusCode() {
        assertThat(response.getStatusCode(), equalTo(200));
    }

    // 2. Playback Response
    @Test(priority = 2)
    public void validatePlayback() {

        String playbackUrl = json.getString("playback[0].playback_url");
        Assert.assertNotNull(playbackUrl);
        Assert.assertTrue(playbackUrl.startsWith("https://"));
    }

    // 3. Values
    @Test(priority = 3)
    public void validateValues() {
        Assert.assertNotNull(json.getString("values.pid"));
        Assert.assertNotNull(json.getString("values.protection_scheme"));
        Assert.assertNotNull(json.getString("values.kid"));
        Assert.assertNotNull(json.getString("values.token"));
    }

    // 4. Subtitles
    @Test(priority = 4)
    public void validateSubtitleLanguages() {
        Assert.assertNotNull(json.getString("subtitles[0].content"));
    }

    // 5. enableWebsocket
    @Test(priority = 5)
    public void validateWebSocket() {
        Assert.assertNotNull(json.getBoolean("enableWebsocket"));
    }
    // 5. responseTime
    @Test(priority = 6)
    public void validateResponseTime() {
        long responseTime = response.getTime(); // ms
        assertThat("Response time should be < 250ms", responseTime, lessThan(250L));
    }
}
