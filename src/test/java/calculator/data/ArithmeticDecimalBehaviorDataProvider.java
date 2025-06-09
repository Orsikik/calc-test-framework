package calculator.data;

import model.TestCaseData;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.List;

public class ArithmeticDecimalBehaviorDataProvider extends BaseDataProvider {

    @DataProvider(name = "decimalArithmeticData")
    public static Object[][] provideDecimalArithmeticData(Method method) {
        return filterByTestId(method, List.of(
                TestCaseData.fromStringInput(
                        "020",
                        () -> "1.5 + 2.25",
                        "3.75",
                        "Basic decimal addition"
                ),
                TestCaseData.fromStringInput(
                        "021",
                        () -> "scale=4; 2.25 * 3.1",
                        "6.975",
                        "Decimal multiplication with scale"
                ),
                TestCaseData.fromStringInput(
                        "022",
                        () -> "scale=50; 1/7",
                        ".14285714285714285714285714285714285714285714285714",
                        "High-precision division should return 50 decimal digits"
                ),
                TestCaseData.fromStringInput(
                        "023",
                        () -> "scale=5; 1.2 + 0",
                        "1.2",
                        "Decimal normalization — no trailing zero preservation"
                ),
                TestCaseData.fromStringInput(
                        "024",
                        () -> "scale=2; 10 / 4",
                        "2.50",
                        "Division with decimal result should produce a decimal"
                ),
                TestCaseData.fromStringInput(
                        "025",
                        () -> "2 + 3 * 4 - 5",
                        "9",
                        "Mixed operations should follow operator precedence"
                ),
                TestCaseData.fromStringInput(
                        "026",
                        () -> "(2 + 3) * (4 - 5)",
                        "-5",
                        "Parentheses should affect operation order correctly"
                ),
                TestCaseData.fromStringInput(
                        "027",
                        () -> "5 + 6 - 3 * 2 / 1",
                        "5",
                        "Full expression should be evaluated respecting operator precedence"
                )
        ));
    }
}

