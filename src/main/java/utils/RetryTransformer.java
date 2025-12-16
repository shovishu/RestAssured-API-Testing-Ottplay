package utils;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryTransformer implements IAnnotationTransformer {

    @Override
    public void transform(
            ITestAnnotation annotation,
            Class testClass,
            Constructor testConstructor,
            Method testMethod) {

        // Apply retry globally
        annotation.setRetryAnalyzer(ApiRetryAnalyzer.class);
    }
}
