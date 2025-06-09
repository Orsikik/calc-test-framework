package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static utils.LoggingService.getLogger;

public class StreamHandler extends Thread {
    private final InputStream inputStream;
    private final StringBuilder output = new StringBuilder();

    public StreamHandler(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        } catch (IOException e) {
            getLogger().warn("⚠️ StreamHandler encountered an IOException: {}", e.getMessage());
        }
    }

    public String getOutput() {
        return output.toString();
    }

    public List<String> getLines() {
        return List.of(getOutput());
    }
}
