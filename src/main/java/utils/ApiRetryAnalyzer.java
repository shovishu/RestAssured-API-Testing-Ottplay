package utils;

import lombok.Getter;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

@Getter
public class ApiRetryAnalyzer implements IRetryAnalyzer {

    // ✅ Add these getters to allow listener access
    private int retryCount = 0;
    private static final int MAX_RETRY = 2;

    @Override
    public boolean retry(ITestResult result) {

        if (retryCount >= MAX_RETRY) {
            return false;
        }

        Throwable throwable = result.getThrowable();

        // 🧪 TEMP LOGIC – intentional assertion failure
        if (throwable instanceof AssertionError
                && throwable.getMessage() != null
                && throwable.getMessage().contains("Found null _id")) {

            retryCount++;
            System.out.println("🧪 Retry due to intentional failure: "
                    + result.getName()
                    + " | attempt " + retryCount);
            return true;
        }

        // 1️⃣ Retry for network-related exceptions
        if (throwable != null) {
            if (throwable instanceof SocketTimeoutException
                    || throwable instanceof ConnectException
                    || throwable instanceof UnknownHostException) {

                retryCount++;
                System.out.println("🌐 Network retry: "
                        + result.getName()
                        + " | attempt " + retryCount);
                return true;
            }
        }

        // 2️⃣ Retry for transient HTTP status codes
        Object status = result.getAttribute("statusCode");

        if (status instanceof Integer) {
            int statusCode = (int) status;

            if (statusCode >= 500 || statusCode == 429) {
                retryCount++;
                System.out.println("🔁 HTTP retry (" + statusCode + "): "
                        + result.getName()
                        + " | attempt " + retryCount);
                return true;
            }
        }

        // ❌ Do not retry deterministic failures
        return false;
    }

    public int getMaxRetry() {
        return MAX_RETRY;
    }
}
