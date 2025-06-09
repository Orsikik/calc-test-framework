package calculator;

public class EvaluationResult {
    private final String output;
    private final String error;
    private final boolean success;

    public EvaluationResult(String output, String error) {
        this.output = output;
        this.error = error;
        this.success = (error == null || error.isEmpty());
    }

    public String getOutput() {
        return output;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return success
                ? "✅ Result: " + output
                : "❌ Error: " + error;
    }
}