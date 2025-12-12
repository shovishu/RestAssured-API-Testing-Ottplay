package auth;

import clients.LoginClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Login Module")
@Feature("Login User API")
@Severity(SeverityLevel.CRITICAL)
public class LoginTests {
    public static Response response;
    public static String authToken;
    public static String clientId;

    @Attachment(value = "API Response", type = "application/json")
    public String attachResponse(String res) {
        return res;
    }

    @Attachment(value = "API Request", type = "text/plain")
    public String attachRequest(String req) {
        return req;
    }

    @BeforeClass
    public void setUp() {
        response = LoginClient.login();

        Response response = LoginClient.login();
        authToken = response.jsonPath().getString("headers.authorization");
        clientId = response.jsonPath().getString("data.clientId");

        attachRequest(
                "token=" + authToken +
                        "\nclientId=" + clientId);

        attachResponse(response.asString());

    }

    @Test(priority = 1, description = "Verify that the response status code is 200")
    public void verifyStatusCode() {
        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch!");
    }

    @Test(priority = 2, description = "Verify that the success flag in the response is true")
    public void verifyIfRequestSuccessful() {
        boolean success = response.jsonPath().getBoolean("success");
        Assert.assertTrue(success, "Success flag should be true");
    }

    @Test(priority = 3, description = "Verify that the response contains a valid authorization token")
    public void verifyAuthToken() {
        String token = response.jsonPath().getString("headers.authorization");
        Assert.assertTrue(token != null && !token.isEmpty(), "Response missing authorization token!");
    }

    @Test(priority = 4, description = "Verify that user details (name, clientId, mobileNumber, cellNumber, emailId) are present in the response")
    public void verifyUserDetails() {
        String name = response.jsonPath().getString("data.name");
        String clientId = response.jsonPath().getString("data.clientId");
        String mobileNumber = response.jsonPath().getString("data.mobileNumber");
        String cellNumber = response.jsonPath().getString("data.cellNumber");
        String emailId = response.jsonPath().getString("data.email_id");

        Assert.assertNotNull(name, "Name should not be null");
        Assert.assertNotNull(clientId, "ClientId should not be null");
        Assert.assertNotNull(mobileNumber, "Mobile number should not be null");
        Assert.assertNotNull(cellNumber, "Cell number should not be null");
        Assert.assertNotNull(emailId, "Email ID should not be null");
    }

    @Test(priority = 5, description = "Verify that the user's subscription status is true")
    public void VerifySubscriptionStatus() {
        boolean subscription = response.jsonPath().getBoolean("data.subscription");
        Assert.assertTrue(subscription, "Subscription should be true");
    }

    @Test(priority = 6, description = "Verify that the user's plan details (planId, planName, status, planType, expiryDate) are correct and plan status is active")
    public void VerifyPlanDetails() {
        String planId = response.jsonPath().getString("data.planData._id");
        String planName = response.jsonPath().getString("data.planData.name");
        String planStatus = response.jsonPath().getString("data.planData.status");
        String planType = response.jsonPath().getString("data.planData.subscription_plan");
        String expiryDate = response.jsonPath().getString("data.planData.expiry_date");

        Assert.assertNotNull(planId, "Plan ID should not be null");
        Assert.assertNotNull(planName, "Plan Name should not be null");
        Assert.assertEquals(planStatus, "active", "Plan status should be active");
        Assert.assertNotNull(planType, "Subscription plan should not be null");
        Assert.assertNotNull(expiryDate, "Plan expiry date should not be null");
    }

    @Test(priority = 7, description = "Verify Response Time")
    public void responseTimeTest() {
        long responseTime = response.getTime(); // ms
        System.out.println(responseTime);
        Assert.assertTrue(responseTime < 5000L, "Response time should be < 250ms");
    }

}

