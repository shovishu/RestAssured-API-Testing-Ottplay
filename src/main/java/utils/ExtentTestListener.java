package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentTestListener implements ITestListener, IInvokedMethodListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<String, ExtentTest> extentTestMap = new ConcurrentHashMap<>();

    // Map to store start time for each test
    private Map<ITestResult, Long> startTimes = new HashMap<>();

    @Override
    public void onTestStart(ITestResult result) {
        String className = result.getTestClass().getName();
        String methodName = result.getMethod().getMethodName();
        String testName = className + "." + methodName; // ensures uniqueness
        String description = result.getMethod().getDescription();
        ExtentTest extentTest = extentTestMap.get(testName);

        if (extentTest == null) {
            // create new test
            if (description != null && !description.isEmpty()) {
                extentTest = extent.createTest(methodName, description);
            } else {
                extentTest = extent.createTest(methodName);
            }

            extentTest.assignAuthor("Vishwajeet Singh");
            extentTest.assignDevice(System.getProperty("os.name"));

            // ✅ Assign category based on package (feature folder)
            String packageName = result.getMethod().getRealClass().getPackage().getName();
            String[] packageParts = packageName.split("\\.");
            String featureName = packageParts[packageParts.length - 1]; // e.g. auth, like, ManageProfiles
            extentTest.assignCategory(featureName);

            // ✅ Assign TestNG groups (if any)
            for (String group : result.getMethod().getGroups()) {
                extentTest.assignCategory(group);
            }

            extentTestMap.put(testName, extentTest);
        }

        // Thread-safe storage for current test
        test.set(extentTest);
    }



    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed ✅");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail("Test Failed ❌: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test Skipped ⚠️: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();  // write results at the end
    }

    // Optional getter so you can log inside your tests:
    public static ExtentTest getTest() {
        return test.get();
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        testResult.setAttribute("startTime", System.currentTimeMillis());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        Long startTime = (Long) testResult.getAttribute("startTime");
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;

            String msg = "⏱ Execution time for " + method.getTestMethod().getMethodName() + " → " + duration + " ms";

            // Log to console
            System.out.println(msg);

            // Log to ExtentReport if this is a test method
            if (method.isTestMethod() && test.get() != null) {
                test.get().log(Status.INFO, msg);
            }
//
//            // Attach execution time to Allure report
//            if (method.isTestMethod()) {
//                Allure.addAttachment("Execution Time", msg);
//            }
        }
    }

}
