package calculator.data;

import model.TestCaseData;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

public abstract class BaseDataProvider {

    protected static String extractIdFromDescription(String description) {
        if (description == null) return "";
        int start = description.indexOf("ID: ");
        if (start == -1) return "";
        int end = description.indexOf(" ", start + 4);
        if (end == -1) end = description.length();
        return description.substring(start + 4, end).trim();
    }

    protected static Object[][] filterByTestId(Method testMethod, List<TestCaseData> allCases) {
        String testId = extractIdFromDescription(
                testMethod.getAnnotation(Test.class).description()
        );
        return allCases.stream()
                .filter(tc -> tc.id().equals(testId))
                .map(tc -> new Object[]{tc})
                .toArray(Object[][]::new);
    }
}