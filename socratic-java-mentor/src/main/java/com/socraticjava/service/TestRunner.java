package com.socraticjava.service;

import com.socraticjava.model.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service for running test cases against compiled student code
 */
public class TestRunner {

    /**
     * Result of running a single test case
     */
    public static class TestResult {
        private final TestCase testCase;
        private final boolean passed;
        private final Object actualOutput;
        private final String errorMessage;

        public TestResult(TestCase testCase, boolean passed, Object actualOutput, String errorMessage) {
            this.testCase = testCase;
            this.passed = passed;
            this.actualOutput = actualOutput;
            this.errorMessage = errorMessage;
        }

        public TestCase getTestCase() {
            return testCase;
        }

        public boolean isPassed() {
            return passed;
        }

        public Object getActualOutput() {
            return actualOutput;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * Result of running all test cases
     */
    public static class TestSuiteResult {
        private final List<TestResult> testResults;
        private final int passedCount;
        private final int totalCount;

        public TestSuiteResult(List<TestResult> testResults) {
            this.testResults = testResults;
            this.passedCount = (int) testResults.stream().filter(TestResult::isPassed).count();
            this.totalCount = testResults.size();
        }

        public List<TestResult> getTestResults() {
            return testResults;
        }

        public int getPassedCount() {
            return passedCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public boolean allPassed() {
            return passedCount == totalCount;
        }
    }

    /**
     * Runs all test cases against the compiled code
     * @param compiledClasses Map of class names to bytecode
     * @param className The main class to execute
     * @param testCases List of test cases to run
     * @return TestSuiteResult with results for each test
     */
    public TestSuiteResult runTests(Map<String, byte[]> compiledClasses, String className,
                                     List<TestCase> testCases) {
        List<TestResult> results = new ArrayList<>();

        try {
            // Create custom class loader for the compiled code
            InMemoryClassLoader classLoader = new InMemoryClassLoader(compiledClasses);
            Class<?> clazz = classLoader.loadClass(className);

            // For simple programs, we'll run the main method and capture output
            Method mainMethod = clazz.getMethod("main", String[].class);

            for (TestCase testCase : testCases) {
                TestResult result = runSingleTest(mainMethod, testCase);
                results.add(result);
            }

        } catch (Exception e) {
            // If we can't load or execute the class, fail all tests
            for (TestCase testCase : testCases) {
                results.add(new TestResult(testCase, false, null,
                    "Failed to execute code: " + e.getMessage()));
            }
        }

        return new TestSuiteResult(results);
    }

    /**
     * Runs a single test case
     */
    private TestResult runSingleTest(Method mainMethod, TestCase testCase) {
        try {
            // Capture System.out
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outputStream));

            try {
                // Execute the main method with inputs
                String[] args = convertInputsToStringArray(testCase.getInputs());
                mainMethod.invoke(null, (Object) args);

                // Get the output
                String actualOutput = outputStream.toString().trim();

                // Compare with expected output
                String expectedOutput = testCase.getExpectedOutput().toString();
                boolean passed = actualOutput.equals(expectedOutput);

                return new TestResult(testCase, passed, actualOutput,
                    passed ? "" : "Expected: " + expectedOutput + "\nActual: " + actualOutput);

            } finally {
                // Restore System.out
                System.setOut(originalOut);
            }

        } catch (Exception e) {
            return new TestResult(testCase, false, null,
                "Runtime error: " + e.getMessage());
        }
    }

    /**
     * Converts test inputs to String array for main method
     */
    private String[] convertInputsToStringArray(Object[] inputs) {
        if (inputs == null || inputs.length == 0) {
            return new String[0];
        }

        String[] stringArgs = new String[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            stringArgs[i] = inputs[i].toString();
        }
        return stringArgs;
    }

    /**
     * Custom class loader that loads classes from byte arrays
     */
    private static class InMemoryClassLoader extends ClassLoader {
        private final Map<String, byte[]> classes;

        public InMemoryClassLoader(Map<String, byte[]> classes) {
            this.classes = classes;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] classBytes = classes.get(name);
            if (classBytes == null) {
                throw new ClassNotFoundException(name);
            }
            return defineClass(name, classBytes, 0, classBytes.length);
        }
    }
}
