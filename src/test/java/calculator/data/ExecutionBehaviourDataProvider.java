package calculator.data;

import model.TestCaseData;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.List;


public class ExecutionBehaviourDataProvider extends BaseDataProvider {

    @DataProvider(name = "executionBehaviourData")
    public static Object[][] provideBehaviourData(Method method) {
        return filterByTestId(method, List.of(
                TestCaseData.fromStringInput(
                        "011",
                        () -> "",
                        "",
                        "Empty input — should be handled gracefully"
                ),
                TestCaseData.fromStringInput(
                        "012",
                        () -> "1 + * 2",
                        "",
                        "Invalid syntax — should return parser error or descriptive message"
                ),
                TestCaseData.fromStringInput(
                        "013",
                        () -> "1 / 0",
                        "",
                        "Division by zero — should return runtime error or specific message"
                ),
                TestCaseData.fromStringInput(
                        "014",
                        () -> "a + 1",
                        "",
                        "Undefined variable — should return error about undefined symbol"
                ),
                TestCaseData.fromStringInput(
                        "015",
                        () -> "(1 + (2 + 3)",
                        "",
                        "Unterminated parentheses — should trigger syntax or parser error"
                ),
                TestCaseData.fromStringInput(
                        "016",
                        () -> "1+1\nabc\n2+2",
                        "",
                        "Mixed valid and invalid lines — some processed, errors reported for others"
                ),
                TestCaseData.fromStringInput(
                        "017",
                        () -> "5 +",
                        "",
                        "Expression ending with operator — should produce syntax error or request for missing operand"
                ),
                TestCaseData.fromStringInput(
                        "018",
                        () -> ". + 1",
                        "1",
                        "Misplaced decimal treated as 0 — valid input"
                ),
                TestCaseData.fromStringInput(
                        "019",
                        () -> "scale=abc; 1/2",
                        "0",
                        "Malformed scale assignment should fallback to default scale"
                )
        ));
    }
}
