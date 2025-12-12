package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class ApiRetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY = 3;

    @Override
    public boolean retry(ITestResult iTestResult) {

        Throwable cause = iTestResult.getThrowable();

        // Retry if failure caused by authorization problem
        if (cause != null && cause.getMessage().contains("401")) {
            System.out.println("🔄 Retrying due to Unauthorized (401)...");
            return retryCount++ < MAX_RETRY;
        }
        // Retry for any unexpected intermittent API failures
        return retryCount++ < MAX_RETRY;
    }
}
