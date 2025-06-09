package calculator.data;

import model.TestCaseData;
import org.testng.annotations.DataProvider;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArithmeticDataProvider extends BaseDataProvider {

    @DataProvider(name = "arithmeticLimitsData")
    public Object[][] provideSmallExpressions(Method method) {
        return filterByTestId(method, List.of(
                new TestCaseData(
                        "001",
                        "2+2",
                        "4",
                        "Simple addition"
                ),
                new TestCaseData(
                        "003",
                        "scale=5000; 1/3",
                        "Expected 5000 decimals",
                        "Decimal scale within limit"
                ),
                new TestCaseData(
                        "007",
                        "9".repeat(2000) + " * " + "9".repeat(2000),
                        "Expected long result of 2000-digit * 2000-digit",
                        "Correct result of large multiplication with 2000-digit operands"
                ),
                new TestCaseData(
                        "005",
                        () -> new StringReader(
                                IntStream.range(0, 1000)
                                        .mapToObj(i -> "1")
                                        .collect(Collectors.joining(" + "))
                        ),
                        () -> "1000",
                        "1000 additions"
                ),
                TestCaseData.fromStringInput(
                        "010",
                        () -> "(".repeat(100) + "1+2" + ")".repeat(100),
                        "3",
                        "Correct result should be returned even with 100-level nesting"
                )
        ));
    }

    @DataProvider(name = "arithmeticHeavyData")
    public Object[][] provideLargeExpressions(Method method) {
        return filterByTestId(method, List.of(
                TestCaseData.fromReaderInput(
                        "004",
                        () -> new StringReader("scale=1000000000\n1/3"),
                        "Expected error or silent truncation",
                        "Decimal scale exceeds limit"
                ),
                TestCaseData.fromReaderInput(
                        "002",
                        () -> new StringReader("9".repeat(1000000000) + " + 1"),
                        "error or silent overflow",
                        "Integer exceeds limit"
                ),
                TestCaseData.fromReaderInput(
                        "006",
                        ArithmeticDataProvider::oneBillionAdditionsReader,
                        "",
                        "ID: 006 - 1 billion additions — extreme overload for bc"
                ),
                TestCaseData.fromStringInput(
                        "007",
                        () -> "scale=0; " + "9".repeat(2000) + " * " + "9".repeat(2000),
                        "Length > 4000, ends with digits",
                        "Large multiplication of 2000-digit operands"
                ),
                TestCaseData.fromReaderInput(
                        "008",
                        () -> new StringReader("scale=0; " + "9".repeat(1000000000) + " * " + "9".repeat(12000)),
                        "Expected error or truncation",
                        "Too-large multiplication result"
                )
        ));
    }

    private static Reader oneBillionAdditionsReader() {
        return new Reader() {
            private int count = 1_000_000_000;
            private final char[] onePlus = "1+".toCharArray();
            private int pos = 0;

            @Override
            public int read(char[] cbuf, int off, int len) {
                if (count <= 0) return -1;
                int written = 0;
                while (written < len && count > 0) {
                    int copyLen = Math.min(onePlus.length - pos, len - written);
                    System.arraycopy(onePlus, pos, cbuf, off + written, copyLen);
                    pos += copyLen;
                    written += copyLen;
                    if (pos == onePlus.length) {
                        pos = 0;
                        count--;
                    }
                }
                return written > 0 ? written : -1;
            }

            @Override
            public void close() {
            }
        };
    }
}

