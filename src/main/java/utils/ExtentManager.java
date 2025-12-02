package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import baseClass.BaseClass;
import config.ConfigManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {

            // 📂 Create a timestamped folder for each report
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportDir = System.getProperty("user.dir") + "/test-output/ExtentReports_" + timeStamp;
            new File(reportDir).mkdirs();
            String reportPath = reportDir + "/ExtentReport.html";

            // 🧾 Configure ExtentSparkReporter
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("API Automation Test Report");
            sparkReporter.config().setReportName("RestAssured API Suite Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // ✅ Fetch environment details from BaseClass
            String env = BaseClass.getEnv();
            String baseUrl = BaseClass.getBaseUrl();
//            String buildVersion = ConfigManager.get(env + ".buildVersion");

            // ⚙️ Add system info dynamically
            extent.setSystemInfo("Project Name", "OTTPlay API Automation");
            extent.setSystemInfo("Environment", env.toUpperCase());
            extent.setSystemInfo("Base URL", baseUrl);
//            extent.setSystemInfo("Build Version", buildVersion != null ? buildVersion : "N/A");
            extent.setSystemInfo("Executed By", System.getProperty("user.name"));
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Framework", "TestNG + RestAssured");
            extent.setSystemInfo("Report Generated On",
                    new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(new Date()));

            // Optional: Jenkins / Git metadata
            if (System.getenv("BUILD_NUMBER") != null)
                extent.setSystemInfo("Jenkins Build", System.getenv("BUILD_NUMBER"));
            if (System.getenv("GIT_BRANCH") != null)
                extent.setSystemInfo("Git Branch", System.getenv("GIT_BRANCH"));
        }

        return extent;
    }
}
