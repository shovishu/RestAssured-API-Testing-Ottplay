package watchlist;

import baseClass.BaseClass;
import clients.WatchlistClient;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.SessionManager;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetWatchlistTest extends BaseClass {

    @Test(priority = 1)
    public void GetProfileWatchlistTest() {
        // Always fetch from SessionManager
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String userProfileId = SessionManager.getProfileId();

        System.out.println("Token: " + token);
        System.out.println("ClientId: " + clientId);
        System.out.println("User Profile ID: " + userProfileId);

        // Call API
        Response response = WatchlistClient.getWatchlist(token, userProfileId);

        // Assert status code
        assertThat(response.getStatusCode(), equalTo(200));

        // Convert response to Map
        Map<String, Object> responseData = response.jsonPath().getMap("");
        System.out.println("Keys in response: " + responseData.keySet());

        // Validate top-level keys
        assertThat(responseData, allOf(
                hasKey("statusCode"),
                hasKey("isCached"),
                hasKey("currentPage"),
                hasKey("totalDocuments"),
                hasKey("perPage"),
                hasKey("previousPage"),
                hasKey("isReviewed"),
                hasKey("parentalFilter"),
                hasKey("lastPage"),
                hasKey("result")
        ));

        // Validate 'result' array
        List<Map<String, Object>> results = (List<Map<String, Object>>) responseData.get("result");
        assertThat(results, is(not(empty())));

        for (Map<String, Object> item : results) {
            Map<String, Object> moviePref = (Map<String, Object>) item.get("movie_pref");
            assertThat(moviePref, allOf(
                    hasKey("genres"),
                    hasKey("posters"),
                    hasKey("primary_language"),
                    hasKey("reviews"),
                    hasKey("_id"),
                    hasKey("name"),
                    hasKey("permalink"),
                    hasKey("release_year"),
                    hasKey("content_type"),
                    hasKey("where_to_watch"),
                    hasKey("seo_url"),
                    hasKey("ottplay_id"),
                    hasKey("ottplay_rating"),
                    hasKey("is_adult"),
                    hasKey("reviewExist")
            ));

            // Check where_to_watch providers
            List<Map<String, Object>> watchItems = (List<Map<String, Object>>) moviePref.get("where_to_watch");
            for (Map<String, Object> watchItem : watchItems) {
                Map<String, Object> provider = (Map<String, Object>) watchItem.get("provider");
                assertThat(provider, allOf(
                        hasKey("_id"),
                        hasKey("name"),
                        hasKey("status"),
                        hasKey("subscription"),
                        hasKey("free_for_all_provider"),
                        hasKey("seourl")
                ));
            }
        }
    }
}
