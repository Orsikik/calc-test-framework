package model;

import java.io.Reader;
import java.util.function.Supplier;

public class TestCaseData {
    private final String id;
    private final Supplier<String> inputSupplier;
    private final Supplier<String> expectedOutputSupplier;
    private final String description;
    private final Supplier<Reader> streamSupplier;

    public TestCaseData(String id, Supplier<String> inputSupplier, Supplier<String> expectedOutputSupplier,
                        String description, Supplier<Reader> streamSupplier) {
        this.id = id;
        this.inputSupplier = inputSupplier;
        this.expectedOutputSupplier = expectedOutputSupplier;
        this.description = description;
        this.streamSupplier = streamSupplier;
    }

    public TestCaseData(String id, String input, String expectedOutput, String description) {
        this(id, () -> input, () -> expectedOutput, description, null);
    }

    public static TestCaseData fromStringInput(String id, Supplier<String> input, String expectedOutput, String description) {
        return new TestCaseData(id, input, () -> expectedOutput, description, null);
    }

    public static TestCaseData fromReaderInput(String id, Supplier<Reader> reader, String expectedOutput, String description) {
        return new TestCaseData(id, null, () -> expectedOutput, description, reader);
    }

    public TestCaseData(String id, Supplier<Reader> streamSupplier, String expectedOutput, String description) {
        this(id, null, () -> expectedOutput, description, streamSupplier);
    }

    public TestCaseData(String id, Supplier<Reader> streamSupplier, Supplier<String> expectedOutputSupplier, String description) {
        this.id = id;
        this.streamSupplier = streamSupplier;
        this.inputSupplier = null;
        this.expectedOutputSupplier = expectedOutputSupplier;
        this.description = description;
    }

    public String id() {
        return id;
    }

    public String input() {
        return inputSupplier != null ? inputSupplier.get() : null;
    }

    public String getExpectedOutput() {
        return expectedOutputSupplier.get();
    }

    public String getDescription() {
        return description;
    }

    public boolean isStreamed() {
        return streamSupplier != null;
    }

    public Reader getStreamReader() {
        return streamSupplier.get();
    }

}
