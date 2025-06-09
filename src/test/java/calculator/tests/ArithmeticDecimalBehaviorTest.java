package calculator.tests;

import calculator.BcCalculator;
import calculator.CalculatorTool;
import calculator.EvaluationResult;
import calculator.data.ArithmeticDecimalBehaviorDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.testng.AllureTestNg;
import model.TestCaseData;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({AllureTestNg.class})
public class ArithmeticDecimalBehaviorTest extends BaseTest {

    CalculatorTool calculator = new BcCalculator();

    @Story("Decimal arithmetic")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 020 - Basic decimal addition should return correct result")
    @Test(dataProvider = "decimalArithmeticData",
            dataProviderClass = ArithmeticDecimalBehaviorDataProvider.class,
            description = "ID: 020 - Basic decimal addition should return correct result",
            groups = {"decimal", "positive"})
    public void testBasicDecimalAddition(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertEquals(
                result.getOutput().trim(),
                testCase.getExpectedOutput(),
                "Decimal addition result mismatch"
        );
    }

    @Story("Decimal arithmetic")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 021 - Decimal multiplication with scale should return correct result")
    @Test(dataProvider = "decimalArithmeticData",
            dataProviderClass = ArithmeticDecimalBehaviorDataProvider.class,
            description = "ID: 021 - Decimal multiplication with scale should return correct result",
            groups = {"decimal", "positive"})
    public void testDecimalMultiplicationWithScale(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertEquals(
                result.getOutput().trim(),
                testCase.getExpectedOutput(),
                "Decimal multiplication result mismatch"
        );
    }

    @Story("Decimal arithmetic")
    @Severity(SeverityLevel.CRITICAL)
    @Description("ID: 022 - High-precision division should return 50 decimal digits")
    @Test(dataProvider = "decimalArithmeticData",
            dataProviderClass = ArithmeticDecimalBehaviorDataProvider.class,
            description = "ID: 022 - High-precision division should return 50 decimal digits",
            groups = {"decimal", "positive"})
    public void testHighPrecisionDivision(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertTrue(result.isSuccess(), "Evaluation should succeed");

        String output = result.getOutput().trim();
        String expected = testCase.getExpectedOutput();

        assertEquals(output, expected, "Expected high-precision result for 1/7 with scale=50");
    }

    @Story("Decimal arithmetic")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 023 - Decimal normalization should preserve scale formatting")
    @Test(dataProvider = "decimalArithmeticData",
            dataProviderClass = ArithmeticDecimalBehaviorDataProvider.class,
            description = "ID: 023 - Decimal normalization should preserve scale formatting",
            groups = {"decimal", "positive"})
    public void testDecimalNormalization(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);

        assertTrue(result.isSuccess(), "Evaluation should succeed");

        String output = result.getOutput().trim();
        String expected = testCase.getExpectedOutput();

        assertEquals(output, expected, "Expected normalized decimal output with trailing zeros preserved");
    }

    @Story("Decimal arithmetic")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 024 - Division with decimal result should produce a decimal")
    @Test(dataProvider = "decimalArithmeticData",
            dataProviderClass = ArithmeticDecimalBehaviorDataProvider.class,
            description = "ID: 024 - Division with decimal result should produce a decimal",
            groups = {"decimal", "positive"})
    public void testDivisionWithDecimalResult(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertTrue(result.isSuccess(), "Evaluation should succeed");

        String output = result.getOutput().trim();
        String expected = testCase.getExpectedOutput();

        assertEquals(output, expected, "Expected correct decimal result for 10 / 4");
    }

    @Story("Decimal arithmetic")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 025 - Mixed operations should follow operator precedence")
    @Test(dataProvider = "decimalArithmeticData",
            dataProviderClass = ArithmeticDecimalBehaviorDataProvider.class,
            description = "ID: 025 - Mixed operations should follow operator precedence",
            groups = {"decimal", "positive"})
    public void testMixedOperations(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertTrue(result.isSuccess(), "Evaluation should succeed");

        String output = result.getOutput().trim();
        String expected = testCase.getExpectedOutput();

        assertEquals(output, expected, "Expected correct result respecting operator precedence");
    }

    @Story("Decimal arithmetic")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 026 - Parentheses should affect operation order correctly")
    @Test(dataProvider = "decimalArithmeticData",
            dataProviderClass = ArithmeticDecimalBehaviorDataProvider.class,
            description = "ID: 026 - Parentheses should affect operation order correctly",
            groups = {"decimal", "positive"})
    public void testParenthesesAffectingOrder(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertTrue(result.isSuccess(), "Evaluation should succeed");

        String output = result.getOutput().trim();
        String expected = testCase.getExpectedOutput();

        assertEquals(output, expected, "Expected result with parentheses affecting operation order");
    }

    @Story("Decimal arithmetic")
    @Severity(SeverityLevel.NORMAL)
    @Description("ID: 027 - Full expression should be evaluated respecting operator precedence")
    @Test(dataProvider = "decimalArithmeticData",
            dataProviderClass = ArithmeticDecimalBehaviorDataProvider.class,
            description = "ID: 027 - Full expression should be evaluated respecting operator precedence",
            groups = {"decimal", "positive"})
    public void testFullExpressionEvaluation(TestCaseData testCase) {
        EvaluationResult result = calculator.evaluate(testCase);
        assertTrue(result.isSuccess(), "Evaluation should succeed");

        String output = result.getOutput().trim();
        String expected = testCase.getExpectedOutput();

        assertEquals(output, expected, "Expected result from full expression evaluation");
    }

}
