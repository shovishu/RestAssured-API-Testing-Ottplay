package utils;

import clients.AuthClient;
import clients.ProfileClient;
import io.restassured.response.Response;

public class SessionManager {
    private static String authToken;
    private static String clientId;
    private static String profileId;

    public static String getAuthToken() {
        if (authToken == null) {
            authenticate();
        }
        return authToken;
    }

    public static String getClientId() {
        if (clientId == null) {
            authenticate();
        }
        return clientId;
    }

    public static String getProfileId() {
        if (profileId == null) {
            // Directly fetch using authToken and clientId
            String token = getAuthToken();    // safe, does NOT call getProfileId()
            String client = getClientId();    // safe, does NOT call getProfileId()
            Response response = ProfileClient.getUserProfiles(token, client);

            profileId = response.jsonPath().getString("data[0].user_profile_id");

            if (profileId == null) {
                throw new RuntimeException("❌ Could not fetch user_profile_id from profile list.");
            }
        }
        return profileId;
    }

    private static void authenticate() {
        Response response = AuthClient.login();

        // Extract values based on your API response
        authToken = response.jsonPath().getString("headers.authorization");
        clientId = response.jsonPath().getString("data.clientId");

        if (authToken == null || clientId == null) {
            throw new RuntimeException("❌ Login failed. Could not retrieve authToken or clientId.");
        }
    }

}
