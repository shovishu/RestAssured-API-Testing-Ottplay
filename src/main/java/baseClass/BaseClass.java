package baseClass;

import com.aventstack.extentreports.ExtentReports;
import config.ConfigManager;
import lombok.Getter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.ExtentManager;

public class BaseClass {
    private static ExtentReports extent;
    @Getter
    private static final String env = System.getProperty("env", "prod");

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
}
