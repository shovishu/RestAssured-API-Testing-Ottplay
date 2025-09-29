package like;

import baseClass.BaseClass;
import clients.LikeClient;
import clients.WatchlistClient;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.SessionManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetLikesTest extends BaseClass {
    private static Response response;

    @BeforeTest()
    public void setUp() {
        // Always fetch from SessionManager
        String token = SessionManager.getAuthToken();
        String clientId = SessionManager.getClientId();
        String userProfileId = SessionManager.getProfileId();

        System.out.println("Token: " + token);
        System.out.println("ClientId: " + clientId);
        System.out.println("User Profile ID: " + userProfileId);

        response = LikeClient.getLikes(token, userProfileId,clientId);
        response.prettyPrint();
    }
    @Test(priority = 1)
    public void GetProfileLikeTest(){
        assertThat(response.getStatusCode(), equalTo(200));
    }



}

