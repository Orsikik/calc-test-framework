package calculator.tests;

import calculator.BcCalculator;
import calculator.CalculatorTool;
import calculator.EvaluationResult;
import calculator.data.ArithmeticDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.testng.AllureTestNg;
import model.TestCaseData;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Listeners({AllureTestNg.class})
public class ArithmeticLimitsTest extends BaseTest {

    CalculatorTool calculator = new BcCalculator();

    @Story("Integer boundary")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 001 - Integer within 3000-digit limit")
    @Test(dataProvider = "arithmeticLimitsData",
            dataProviderClass = ArithmeticDataProvider.class,
            description = "ID: 001 - Integer within 3000-digit limit",
            groups = {"arithmetic", "positive"})
    public void testIntegerWithinLimit(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertTrue(result.isSuccess(), "Should succeed for input ID: " + testCase.id());
    }

    @Story("Integer boundary")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ID: 002 - Integer exceeds limit, expecting timeout or overflow")
    @Test(dataProvider = "arithmeticHeavyData",
            dataProviderClass = ArithmeticDataProvider.class,
            description = "ID: 002 - Integer exceeds limit",
            groups = {"arithmetic", "negative"})
    public void testIntegerExceedsLimit(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluateWithTimeout(testCase);
        boolean isFailure = !result.isSuccess();
        boolean isSilentOverflow = "0".equals(result.getOutput() != null ? result.getOutput().trim() : "");
        assertTrue(
                isFailure || isSilentOverflow,
                "Expected timeout error or silent overflow (e.g., result = 0), but got: " + result.getOutput()
        );
    }

    @Story("Decimal precision")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 003 - Decimal scale within 5000 digits")
    @Test(dataProvider = "arithmeticLimitsData",
            dataProviderClass = ArithmeticDataProvider.class,
            description = "ID: 003 - Decimal scale within limit",
            groups = {"arithmetic", "positive"})
    public void testDecimalScaleWithinLimit(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertTrue(result.isSuccess(), "Expected evaluation to succeed");
        String output = result.getOutput();
        assertNotNull(output, "Output should not be null");
        output = output.replaceAll("[\\r\\n]", "");
        assertTrue(output.contains("."), "Expected output to contain a decimal point");
        String[] parts = output.split("\\.");
        String fractionalPart = parts[1];
        assertTrue(fractionalPart.length() >= 4999,
                "Expected at least 5000 digits after decimal point, but got: " + fractionalPart.length());
    }

    @Story("Decimal precision")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ID: 004 - Decimal scale exceeds limit. Expecting truncation or error.")
    @Test(dataProvider = "arithmeticHeavyData",
            dataProviderClass = ArithmeticDataProvider.class,
            description = "ID: 004 - Decimal scale exceeds limit. Expected error or silent truncation",
            groups = {"arithmetic", "negative"})
    public void testDecimalScaleExceedsLimit(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluateWithTimeout(testCase);
        assertTrue(
                !result.isSuccess() || result.getOutput() == null || result.getOutput().length() < 100,
                "Expected timeout or truncated result for excessive decimal scale"
        );
    }

    @Story("Operations count")
    @Severity(SeverityLevel.MINOR)
    @Description("ID: 005 - 1000 additions should succeed")
    @Test(dataProvider = "arithmeticLimitsData",
            dataProviderClass = ArithmeticDataProvider.class,
            description = "ID: 005 - 1000 additions",
            groups = {"arithmetic", "positive"})
    public void testThousandAdditions(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertTrue(result.isSuccess(), "Expected successful evaluation");
        assertEquals(result.getOutput().trim(), testCase.getExpectedOutput(), "Incorrect result for 1000 additions");
    }

    @Story("Operations count")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ID: 006 - 1 billion additions. Expecting timeout, error, or incorrect result")
    @Test(dataProvider = "arithmeticHeavyData",
            dataProviderClass = ArithmeticDataProvider.class,
            description = "ID: 006 - 100000000 additions — expecting parser error, timeout or incorrect result",
            groups = {"arithmetic", "negative"})
    public void testTenThousandAdditionsOverlimit(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluateWithTimeout(testCase);

        boolean isTimeout = result.getError() != null && result.getError().contains("Timeout");
        boolean isSilentFailure = result.getOutput() == null || result.getOutput().trim().isEmpty();
        boolean isIncorrectResult = "0".equals(result.getOutput() != null ? result.getOutput().trim() : "");

        assertTrue(
                isTimeout || isSilentFailure || isIncorrectResult,
                "Expected timeout, silent failure, or invalid result (like 0). Got:\nOutput: " + result.getOutput() + "\nError: " + result.getError()
        );
    }

    @Story("Multiplication")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 007 - Multiply two 2000-digit integers. Should succeed")
    @Test(dataProvider = "arithmeticHeavyData",
            dataProviderClass = ArithmeticDataProvider.class,
            description = "ID: 007 - Large multiplication of 2000-digit operands",
            groups = {"arithmetic", "positive"})
    public void testLargeMultiplication(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluateWithTimeout(testCase);

        assertTrue(result.isSuccess(), "Expected large multiplication to succeed");
        assertNotNull(result.getOutput(), "Output should not be null");

        String actual = result.getOutput().replaceAll("[\\r\\n]", "").trim();

        assertTrue(actual.length() > 4000, "Expected result length > 4000, got: " + actual.length());
        assertTrue(actual.matches(".*\\d{5}$"), "Expected result to end with 5 digits, got: " + actual.substring(Math.max(0, actual.length() - 10)));
    }

    @Story("Multiplication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ID: 008 - Multiply two 12000-digit integers. Should fail or truncate.")
    @Test(dataProvider = "arithmeticHeavyData",
            dataProviderClass = ArithmeticDataProvider.class,
            description = "ID: 008 - Too-large multiplication result (e.g., 12000-digit operands)",
            groups = {"arithmetic", "negative"})
    public void testTooLargeMultiplication(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluateWithTimeout(testCase);
        boolean failed = !result.isSuccess();
        boolean tooShort = result.getOutput() == null || result.getOutput().replaceAll("[\\r\\n]", "").trim().length() < 100;

        assertTrue(failed || tooShort,
                "Expected failure or truncation for too-large multiplication, but got full output: "
                        + (result.getOutput() != null ? result.getOutput().substring(0, Math.min(100, result.getOutput().length())) : "null"));
    }

    @Story("Expression depth")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 010 - Valid deep nesting of parentheses")
    @Test(dataProvider = "arithmeticLimitsData",
            dataProviderClass = ArithmeticDataProvider.class,
            description = "ID: 010 - Valid deep nesting of parentheses",
            groups = {"arithmetic", "positive"})
    public void testValidParenthesesNesting(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);

        assertTrue(result.isSuccess(), "Expected evaluation to succeed");
        assertNotNull(result.getOutput(), "Output should not be null");
        String actual = result.getOutput().replaceAll("[\\r\\n]", "").trim();
        String expected = testCase.getExpectedOutput().replaceAll("[\\r\\n]", "").trim();

        assertEquals(actual, expected, "Output mismatch for deeply nested expression");
    }
}


