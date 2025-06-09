package calculator.tests;

import calculator.BcCalculator;
import calculator.CalculatorTool;
import calculator.EvaluationResult;
import calculator.data.ExecutionBehaviourDataProvider;
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
public class ExecutionBehaviourTest extends BaseTest {

    CalculatorTool calculator = new BcCalculator();

    @Story("Execution behavior")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 011 - Empty input should be handled gracefully")
    @Test(dataProvider = "executionBehaviourData",
            dataProviderClass = ExecutionBehaviourDataProvider.class,
            description = "ID: 011 - Empty input should be handled gracefully",
            groups = {"behavior"})
    public void testEmptyInput(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);

        boolean isGraceful = result.getOutput() == null || result.getOutput().trim().isEmpty();
        boolean isNotCrashed = result.getError() == null || result.getError().toLowerCase().contains("empty")
                || result.getError().toLowerCase().contains("no input")
                || result.getError().trim().isEmpty();

        assertTrue(isGraceful && isNotCrashed,
                "Expected graceful handling of empty input, but got:\nOutput: " + result.getOutput() + "\nError: " + result.getError());
    }

    @Story("Execution behavior")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ID: 012 - Invalid syntax should return descriptive parser error")
    @Test(dataProvider = "executionBehaviourData",
            dataProviderClass = ExecutionBehaviourDataProvider.class,
            description = "ID: 012 - Invalid syntax should return descriptive parser error",
            groups = {"behavior", "negative"})
    public void testInvalidSyntax(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertFalse(result.isSuccess(), "Expected evaluation to fail due to invalid syntax.");
        assertTrue(result.getError() != null && !result.getError().isBlank(),
                "Expected a descriptive parser error message, but got none.");
    }

    @Story("Execution behavior")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ID: 013 - Division by zero should trigger a runtime error")
    @Test(dataProvider = "executionBehaviourData",
            dataProviderClass = ExecutionBehaviourDataProvider.class,
            description = "ID: 013 - Division by zero should trigger a runtime error",
            groups = {"behavior", "negative"})
    public void testDivisionByZero(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);

        assertFalse(result.isSuccess(), "Expected failure due to division by zero.");
        assertTrue(result.getError() != null && !result.getError().isBlank(),
                "Expected a runtime error message for division by zero.");
    }

    @Story("Execution behavior")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 015 - Unterminated parentheses should produce syntax or parser error")
    @Test(dataProvider = "executionBehaviourData",
            dataProviderClass = ExecutionBehaviourDataProvider.class,
            description = "ID: 015 - Unterminated parentheses should produce syntax or parser error",
            groups = {"behavior", "negative"})
    public void testUnterminatedParentheses(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);

        assertFalse(result.isSuccess(), "Expected failure due to unterminated parentheses.");
        assertTrue(result.getError() != null && !result.getError().isBlank(),
                "Expected a descriptive parser error due to unterminated parentheses.");
    }

    @Story("Execution behavior")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 016 - Mixed valid and invalid lines should partially execute or report errors")
    @Test(dataProvider = "executionBehaviourData",
            dataProviderClass = ExecutionBehaviourDataProvider.class,
            description = "ID: 016 - Mixed valid and invalid lines should partially execute or report errors",
            groups = {"behavior"})
    public void testMixedValidAndInvalidLines(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);

        boolean hasOutput = result.getOutput() != null && !result.getOutput().isBlank();
        boolean hasError = result.getError() != null && !result.getError().isBlank();

        assertTrue(hasOutput || hasError,
                "Expected at least partial output or error message. Got neither.\nOutput: "
                        + result.getOutput() + "\nError: " + result.getError());
    }

    @Story("Execution behavior")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 017 - Expression ending with operator should produce syntax error")
    @Test(dataProvider = "executionBehaviourData",
            dataProviderClass = ExecutionBehaviourDataProvider.class,
            description = "ID: 017 - Expression ending with operator should produce syntax error",
            groups = {"behavior"})
    public void testExpressionEndingWithOperator(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);

        boolean hasError = result.getError() != null && !result.getError().isBlank();

        assertTrue(hasError,
                "Expected a syntax error for expression ending with operator, but no error was reported.\nOutput: "
                        + result.getOutput() + "\nError: " + result.getError());
    }

    @Story("Execution behavior")
    @Severity(SeverityLevel.MINOR)
    @Description("ID: 018 - Misplaced decimal point should be treated as 0")
    @Test(dataProvider = "executionBehaviourData",
            dataProviderClass = ExecutionBehaviourDataProvider.class,
            description = "ID: 018 - Misplaced decimal point should be treated as 0",
            groups = {"behavior"})
    public void testMisplacedDecimalPoint(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);

        assertEquals(result.getOutput().trim(), testCase.getExpectedOutput(),
                "Expected bc to treat misplaced decimal as 0 and return 1");
    }

    @Story("Execution behavior")
    @Severity(SeverityLevel.MINOR)
    @Description("ID: 019 - Malformed scale assignment should fallback to default scale")
    @Test(dataProvider = "executionBehaviourData",
            dataProviderClass = ExecutionBehaviourDataProvider.class,
            description = "ID: 019 - Malformed scale assignment should fallback to default scale",
            groups = {"behavior"})
    public void testMalformedScaleAssignment(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);

        assertEquals(result.getOutput().trim(), testCase.getExpectedOutput(),
                "Expected fallback to default scale (scale=0) when malformed scale assignment provided.");
    }
}
