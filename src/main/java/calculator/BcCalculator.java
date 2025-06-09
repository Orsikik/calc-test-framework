package calculator;

import model.TestCaseData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.concurrent.*;

import static utils.LoggingService.getLogger;

public class BcCalculator implements CalculatorTool {

    public EvaluationResult evaluate(TestCaseData testCase) {
        if (testCase.isStreamed()) {
            return evaluateInternal(null, testCase.getStreamReader());
        } else {
            return evaluateInternal(testCase.input(), null);
        }
    }

    public EvaluationResult evaluateWithTimeout(TestCaseData testCase) {
        try {
            return runWithTimeout(() -> evaluate(testCase), 2, "bc evaluation");
        } catch (TimeoutException e) {
            getLogger().error(e.getMessage());
            return new EvaluationResult(null, e.getMessage());
        } catch (Exception e) {
            return new EvaluationResult(null, "Execution failed: " + e.getMessage());
        }
    }

    private EvaluationResult evaluateInternal(String expression, Reader streamReader) {
        getLogger().info("🚀 [evaluate] Launching bc process");
        try {
            Process process = new ProcessBuilder("bc").start();

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                if (streamReader != null) {
                    getLogger().info("📥 Streaming input directly from Reader");
                    char[] buffer = new char[8192];
                    int read;
                    while ((read = streamReader.read(buffer)) != -1) {
                        writer.write(buffer, 0, read);
                    }
                } else if (expression != null) {
                    writer.write(expression);
                    writer.newLine();
                }

                writer.flush();
            }

            StreamHandler stdoutHandler = new StreamHandler(process.getInputStream());
            StreamHandler stderrHandler = new StreamHandler(process.getErrorStream());
            stdoutHandler.start();
            stderrHandler.start();

            stdoutHandler.join();
            stderrHandler.join();

            String output = stdoutHandler.getOutput().replaceAll("[\\r\\n]", "");
            String error = String.join("\n", stderrHandler.getLines()).trim();
            if (!error.isEmpty()) {
                getLogger().error("🛑 STDERR from bc:\n{}", error);
            }

            String outputPreview = output.length() > 100
                    ? output.substring(0, 100) + "... (truncated)"
                    : output;

            String outputTail = output.length() > 20
                    ? output.substring(output.length() - 10)
                    : output;

            getLogger().info("📤 Result received. Output preview: '{}', ends with: '{}', length: {}, error length: {}",
                    outputPreview, outputTail, output.length(), error.length());

            return new EvaluationResult(output, error);

        } catch (IOException | InterruptedException e) {
            return new EvaluationResult(null, "Exception: " + e.getMessage());
        }
    }

    public static <T> T runWithTimeout(Callable<T> task, int timeoutSeconds, String taskName) throws TimeoutException, Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            getLogger().info("⏳ Starting task '{}' with timeout of {}s", taskName, timeoutSeconds);
            Future<T> future = executor.submit(task);
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new TimeoutException("⏱️ Timeout: " + taskName + " exceeded " + timeoutSeconds + "s");
        } catch (ExecutionException e) {
            throw new Exception("Execution error in " + taskName + ": " + e.getCause().getMessage(), e.getCause());
        } finally {
            executor.shutdownNow();
        }
    }
}
