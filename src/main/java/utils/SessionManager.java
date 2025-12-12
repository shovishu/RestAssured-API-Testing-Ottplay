package utils;

import clients.LoginClient;
import clients.ProfileClient;
import io.restassured.response.Response;
import lombok.Setter;

public class SessionManager {

    private static String authToken;
    private static String clientId;
    private static String profileId;
    private static String newProfileId;

    private static final Object lock = new Object();

    @Setter
    private static String createdProfileId;

    public static String getCreatedProfileId() {
        if (createdProfileId == null) {
            throw new RuntimeException("No profileId available to delete");
        }
        return createdProfileId;
    }

    // ------------------------------------------------------------------------------------
    // PUBLIC GETTERS
    // ------------------------------------------------------------------------------------

    public static synchronized String getAuthToken() {
        if (authToken == null)
            authenticate();     // ONLY logs in once
        return authToken;
    }

    public static synchronized String getClientId() {
        if (clientId == null)
            authenticate();
        return clientId;
    }

    public static synchronized String getProfileId() {

        if (profileId == null) {
            Response response = executeWithTokenRefresh(() ->
                    ProfileClient.getUserProfiles(getAuthToken(), getClientId())
            );

            profileId = response.jsonPath().getString("data[0].user_profile_id");

            if (profileId == null)
                throw new RuntimeException("❌ Could not fetch user_profile_id.");
        }

        return profileId;
    }

    // ------------------------------------------------------------------------------------
    // TOKEN REFRESH WRAPPER (MAGIC PART)
    // ------------------------------------------------------------------------------------

    /**
     * Runs an API request and refreshes token automatically if 401 occurs.
     */
    private static Response executeWithTokenRefresh(ApiRequest action) {
        Response response = action.execute();

        // If token expired -> refresh & retry the SAME API once
        if (response.getStatusCode() == 401) {
            System.out.println("⚠ Token expired → refreshing token...");

            synchronized (lock) {
                authToken = null;     // clear old token
                clientId = null;
                authenticate();       // login again
            }

            return action.execute();  // retry once with new token
        }

        return response;
    }

    // Functional interface for any API request
    @FunctionalInterface
    private interface ApiRequest {
        Response execute();
    }

    // ------------------------------------------------------------------------------------
    // LOGIN (called only once or after expiry)
    // ------------------------------------------------------------------------------------

    private static void authenticate() {
        System.out.println("🔥 LOGIN API CALLED (authenticate executed)");

        Response response = LoginClient.login();

        authToken = response.jsonPath().getString("headers.authorization");
        clientId  = response.jsonPath().getString("data.clientId");

        if (authToken == null || clientId == null)
            throw new RuntimeException("❌ Login failed. Missing authToken/clientId.");
    }
}
