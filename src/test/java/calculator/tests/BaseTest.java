package calculator.tests;

import model.TestCaseData;
import org.slf4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.LoggingService;

import java.lang.reflect.Method;

public class BaseTest {
    protected final Logger logger = LoggingService.getLogger();

    @BeforeMethod
    public void logTestStart(Method method, Object[] testData) {
        System.setProperty("allure.results.directory", "./allure-results");
        String methodName = method.getName();
        String description = "";

        if (method.isAnnotationPresent(Test.class)) {
            description = method.getAnnotation(Test.class).description();
        }

        String id = "N/A";
        if (testData.length > 0 && testData[0] instanceof TestCaseData) {
            id = ((TestCaseData) testData[0]).id();
        }

        logger.info("🚀 Starting test — ID: [{}], Method: [{}], Description: {}", id, methodName, description);
    }

    @AfterMethod
    public void logTestResult(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String status;
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                status = "✅ PASSED";
                break;
            case ITestResult.FAILURE:
                status = "❌ FAILED";
                break;
            case ITestResult.SKIP:
                status = "⚠️ SKIPPED";
                break;
            default:
                status = "❓ UNKNOWN";
        }

        logger.info("{} — Method: [{}]", status, methodName);
    }
}
