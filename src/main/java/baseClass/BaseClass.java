package baseClass;

import com.aventstack.extentreports.ExtentReports;
import config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.ExtentManager;

import static io.restassured.RestAssured.given;

public class BaseClass {
    private static ExtentReports extent;
    @Getter
    private static final String env = System.getProperty("env", "preprod");

    @BeforeSuite
    public void setupSuite() {
        extent = ExtentManager.getInstance();
    }

    @AfterSuite
    public void tearDownSuite() {
        extent.flush();  // writes everything to the HTML file
    }

    public static String getBaseUrl() {
        return ConfigManager.get(env + ".baseURI");
    }

    public static String getCellNumber() {
        return ConfigManager.get(env + ".cellNumber");
    }

    public static String getPassword() {
        return ConfigManager.get(env + ".password");
    }


    //1️⃣ Common base spec (no auth, no context)
    protected static RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .addHeader("accept", "application/json")
                .build();
    }

    //2️⃣ Login / unauthenticated APIs
    protected static RequestSpecification unauthSpec() {
        return given()
                .spec(baseSpec())
                .contentType(ContentType.JSON)
                .header("x-client", String.valueOf(1003))
                .header("platform", "web");
    }

    //3️⃣ Authenticated APIs
    protected static RequestSpecification authSpec(String token) {
        return given()
                .spec(baseSpec())
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .header("auth", token);
    }

    //4️⃣ Auth + client  context
    protected static RequestSpecification authWithClientSpec(
            String token, String clientId) {

        return given()
                .spec(authSpec(token))
                .header("client_id", clientId);
    }

    //5 Auth + profile context
    protected static RequestSpecification authWithProfileSpec(
            String token, String profileId, String clientId) {

        return given()
                .spec(authWithClientSpec(token, clientId))
                .header("profile_id", profileId);
    }



}
