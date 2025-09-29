package player;

import clients.MediaProgressClient;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MediaProgressTests {

    private Response response;  // class-level field
    private JsonPath json;      // class-level field

    @BeforeClass
    public void setUp() {
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String profileId = SessionManager.getProfileId();
        System.out.println("Token: " + token);
        System.out.println("Cline Id: " + clientId);

        response = MediaProgressClient.getMediaProgress(token, clientId,profileId);

//        // Print raw response
//        String body = response.asString();
//        System.out.println("Full Response:");
//        System.out.println(body);
//
//        // Validate content-type before parsing
//        if (response.getHeader("Content-Type").contains("application/json")) {
//            json = response.jsonPath();
//        } else {
//            throw new RuntimeException("Response is not JSON! Body: " + body);
//        }
    }



    // 1️⃣ Validate status code and response time
    @Test
    public void testStatusCodeAndResponseTime() {
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.getTime() <= 2000, "Response time exceeded 2000ms");
    }

//    // 2️⃣ Playback array and URL validation
//    @Test
//    public void testPlaybackUrl() {
//        List<String> playbackUrls = json.getList("playback.playback_url");
//        Assert.assertNotNull(playbackUrls, "Playback array should exist");
//        Assert.assertTrue(playbackUrls.size() > 0, "Playback array should not be empty");
//
//        for (String url : playbackUrls) {
//            Assert.assertNotNull(url, "Playback URL should not be null");
//            Assert.assertTrue(url.startsWith("https://"), "Playback URL should be valid: " + url);
//        }
//    }
//
//    // 3️⃣ Values section validation
//    @Test
//    public void testValuesSection() {
//        Assert.assertNotNull(json.getString("values.pid"), "pid should not be null");
//        Assert.assertNotNull(json.getString("values.protection_scheme"), "protection_scheme should not be null");
//        Assert.assertNotNull(json.getString("values.kid"), "kid should not be null");
//        Assert.assertNotNull(json.getString("values.token"), "token should not be null");
//    }
//
//    // 4️⃣ Subtitles validation
//    @Test
//    public void testSubtitles() {
//        List<String> subtitleUrls = json.getList("subtitles.content");
//        Assert.assertNotNull(subtitleUrls, "Subtitles array should exist");
//
//        for (String url : subtitleUrls) {
//            Assert.assertNotNull(url, "Subtitle URL should not be null");
//            Assert.assertTrue(url.startsWith("https://"), "Subtitle URL should be valid: " + url);
//        }
//    }
//
//    // 5️⃣ enableWebsocket flag
//    @Test
//    public void testEnableWebsocket() {
//        Assert.assertNotNull(json.getBoolean("enableWebsocket"), "enableWebsocket should exist");
//    }
//
//    // 6️⃣ Optional: Validate response timestamps (createdAt, updatedAt)
//    @Test
//    public void testTimestamps() throws ParseException {
//        validateISO8601(json.getString("values.createdAt"), "createdAt");
//        validateISO8601(json.getString("values.updatedAt"), "updatedAt");
//    }
//
//    // 7️⃣ Optional: Validate DRM info consistency
//    @Test
//    public void testDrmInfo() {
//        Assert.assertNotNull(json.getString("values.protection_scheme"), "DRM protection_scheme must exist");
//        Assert.assertNotNull(json.getString("values.kid"), "DRM kid must exist");
//        Assert.assertNotNull(json.getString("values.token"), "DRM token must exist");
//    }
//
//    // 8️⃣ Optional: Validate array sizes or additional flags if needed
//    @Test
//    public void testAdditionalFlags() {
//        Assert.assertTrue(json.getList("playback").size() > 0, "Playback array must not be empty");
//        Assert.assertNotNull(json.getBoolean("enableWebsocket"), "enableWebsocket must exist");
//    }
//
//    // Helper method to validate ISO8601 timestamps
//    private void validateISO8601(String timestamp, String fieldName) throws ParseException {
//        if (timestamp == null) {
//            System.out.println(fieldName + " is null, skipping format validation");
//            return;
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        sdf.setLenient(false);
//        sdf.parse(timestamp);
//    }
}
