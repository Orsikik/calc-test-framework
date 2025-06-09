package runner;

import org.testng.TestNG;

import java.util.Collections;

public class TestMain {
    public static void main(String[] args) {
        TestNG testng = new TestNG();
        testng.setTestSuites(Collections.singletonList("testng.xml"));
        testng.setDefaultSuiteName("CLI Suite");
        testng.setUseDefaultListeners(true);
        testng.run();
        System.out.println("✅ TestNG finished, exiting.");
        System.exit(0);
    }
}