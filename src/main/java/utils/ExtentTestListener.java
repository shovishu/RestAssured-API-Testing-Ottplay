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

        Class<?> clazz = result.getTestClass().getRealClass();
        String className = clazz.getSimpleName();
        String methodName = result.getMethod().getMethodName();

        String testKey = Thread.currentThread().getId() + "_" +
                clazz.getName() + "." + methodName;

        if (extentTestMap.containsKey(testKey)) {
            test.set(extentTestMap.get(testKey));
            return;
        }

        // API name from package
        String apiName = clazz.getPackage().getName(); // auth / like

        // Sub-feature from class name
        String feature = className
                .replace("Test", "")
                .replace("Tests", "");

        // Test display name
        String testName = "[" + feature + "] → " + methodName;

        ExtentTest extentTest = extent.createTest(testName);

        // Author
        String author = result.getTestContext()
                .getCurrentXmlTest()
                .getParameter("author");

        extentTest.assignAuthor(author != null ? author : "Unknown");

        // ✅ ONLY ONE CATEGORY (API)
        extentTest.assignCategory(apiName);

        extentTestMap.put(testKey, extentTest);
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
        }
    }

}
