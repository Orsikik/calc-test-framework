package calculator;


import model.TestCaseData;

public interface CalculatorTool {

    EvaluationResult evaluate(TestCaseData testCase);

    EvaluationResult evaluateWithTimeout(TestCaseData testCase);
}