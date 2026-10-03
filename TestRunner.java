import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Automated test runner for the Simple CLI Calculator.
 * Executes the same 15 scenarios used in the Excel test case tracker,
 * directly against the static methods in Calculator.java, and writes
 * the results to a JSON file for reporting.
 */
public class TestRunner {

    static class TestCase {
        String id, scenario, steps, expected, check;
        double[] inputs;
        double expectedValue;

        TestCase(String id, String scenario, String steps, String expected,
                  String check, double[] inputs, double expectedValue) {
            this.id = id;
            this.scenario = scenario;
            this.steps = steps;
            this.expected = expected;
            this.check = check;
            this.inputs = inputs;
            this.expectedValue = expectedValue;
        }
    }

    static class Result {
        String id, scenario, steps, expected, actual, status;
    }

    public static void main(String[] args) throws IOException {
        List<TestCase> cases = buildTestCases();
        List<Result> results = new ArrayList<>();

        for (TestCase tc : cases) {
            Result r = new Result();
            r.id = tc.id;
            r.scenario = tc.scenario;
            r.steps = tc.steps;
            r.expected = tc.expected;

            String actual;
            boolean pass;
            try {
                switch (tc.id) {
                    case "TC01", "TC02", "TC13" -> {
                        actual = Calculator.formatNumber(Calculator.add(tc.inputs[0], tc.inputs[1]));
                        pass = isClose(actual, tc.expectedValue);
                    }
                    case "TC03", "TC04" -> {
                        actual = Calculator.formatNumber(Calculator.subtract(tc.inputs[0], tc.inputs[1]));
                        pass = isClose(actual, tc.expectedValue);
                    }
                    case "TC05", "TC06", "TC14" -> {
                        actual = Calculator.formatNumber(Calculator.multiply(tc.inputs[0], tc.inputs[1]));
                        pass = isClose(actual, tc.expectedValue);
                    }
                    case "TC10" -> {
                        actual = Calculator.formatNumber(Calculator.power(tc.inputs[0], tc.inputs[1]));
                        pass = isClose(actual, tc.expectedValue);
                    }
                    case "TC07", "TC09", "TC15" -> {
                        actual = Calculator.divide(tc.inputs[0], tc.inputs[1]);
                        pass = isClose(actual, tc.expectedValue);
                    }
                    case "TC08" -> {
                        actual = Calculator.divide(tc.inputs[0], tc.inputs[1]);
                        pass = actual.startsWith("Error");
                    }
                    case "TC11" -> {
                        actual = Calculator.squareRoot(tc.inputs[0]);
                        pass = isClose(actual, tc.expectedValue);
                    }
                    case "TC12" -> {
                        actual = Calculator.squareRoot(tc.inputs[0]);
                        pass = actual.startsWith("Error");
                    }
                    default -> {
                        actual = "UNKNOWN TEST CASE";
                        pass = false;
                    }
                }
            } catch (Exception e) {
                actual = "CRASHED - " + e.getClass().getSimpleName() + ": " + e.getMessage();
                pass = false;
            }

            r.actual = actual;
            r.status = pass ? "PASS" : "FAIL";
            results.add(r);
        }

        int passed = 0;
        for (Result r : results) {
            System.out.println("[" + r.status + "] " + r.id + " - " + r.scenario);
            System.out.println("        Expected: " + r.expected);
            System.out.println("        Actual  : " + r.actual);
            if (r.status.equals("PASS")) passed++;
        }
        System.out.println();
        System.out.println("---------------------------------------------");
        System.out.println("Version under test: " + Calculator.VERSION);
        System.out.println("Total: " + results.size() + "  Passed: " + passed + "  Failed: " + (results.size() - passed));
        System.out.println("---------------------------------------------");

        writeJson(results, "results_v" + Calculator.VERSION + ".json");
        System.out.println("Results written to results_v" + Calculator.VERSION + ".json");
    }

    private static boolean isClose(String actual, double expected) {
        try {
            return Math.abs(Double.parseDouble(actual) - expected) < 1e-6;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void writeJson(List<Result> results, String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"version\": \"").append(Calculator.VERSION).append("\",\n  \"results\": [\n");
        for (int i = 0; i < results.size(); i++) {
            Result r = results.get(i);
            sb.append("    {\n");
            sb.append("      \"id\": \"").append(esc(r.id)).append("\",\n");
            sb.append("      \"scenario\": \"").append(esc(r.scenario)).append("\",\n");
            sb.append("      \"steps\": \"").append(esc(r.steps)).append("\",\n");
            sb.append("      \"expected\": \"").append(esc(r.expected)).append("\",\n");
            sb.append("      \"actual\": \"").append(esc(r.actual)).append("\",\n");
            sb.append("      \"status\": \"").append(esc(r.status)).append("\"\n");
            sb.append("    }").append(i < results.size() - 1 ? "," : "").append("\n");
        }
        sb.append("  ]\n}\n");
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(sb.toString());
        }
    }

    private static String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static List<TestCase> buildTestCases() {
        List<TestCase> list = new ArrayList<>();
        list.add(new TestCase("TC01", "Addition of two positive numbers", "Call add(5, 3)", "8",
                "value", new double[]{5, 3}, 8));
        list.add(new TestCase("TC02", "Addition with a negative number", "Call add(-5, 3)", "-2",
                "value", new double[]{-5, 3}, -2));
        list.add(new TestCase("TC03", "Subtraction of two positive numbers", "Call subtract(10, 4)", "6",
                "value", new double[]{10, 4}, 6));
        list.add(new TestCase("TC04", "Subtraction resulting in a negative number", "Call subtract(3, 10)", "-7",
                "value", new double[]{3, 10}, -7));
        list.add(new TestCase("TC05", "Multiplication of two positive numbers", "Call multiply(6, 7)", "42",
                "value", new double[]{6, 7}, 42));
        list.add(new TestCase("TC06", "Multiplication by zero", "Call multiply(5, 0)", "0",
                "value", new double[]{5, 0}, 0));
        list.add(new TestCase("TC07", "Normal division", "Call divide(10, 2)", "5",
                "value", new double[]{10, 2}, 5));
        list.add(new TestCase("TC08", "Division by zero", "Call divide(10, 0)",
                "Program shows a friendly error message (e.g. 'Error: Cannot divide by zero') and does NOT show Infinity/NaN",
                "graceful_error", new double[]{10, 0}, 0));
        list.add(new TestCase("TC09", "Division producing a decimal result", "Call divide(7, 2)", "3.5",
                "value", new double[]{7, 2}, 3.5));
        list.add(new TestCase("TC10", "Power / exponent calculation", "Call power(2, 3)", "8",
                "value", new double[]{2, 3}, 8));
        list.add(new TestCase("TC11", "Square root of a positive number", "Call squareRoot(16)", "4",
                "value", new double[]{16}, 4));
        list.add(new TestCase("TC12", "Square root of a negative number", "Call squareRoot(-9)",
                "Program shows a friendly error message (e.g. 'Error: Cannot compute square root of a negative number') and does NOT show NaN",
                "graceful_error", new double[]{-9}, 0));
        list.add(new TestCase("TC13", "Addition with decimal numbers", "Call add(2.5, 3.1)", "5.6",
                "value", new double[]{2.5, 3.1}, 5.6));
        list.add(new TestCase("TC14", "Multiplication with large numbers", "Call multiply(123456, 789)", "97406784",
                "value", new double[]{123456, 789}, 97406784));
        list.add(new TestCase("TC15", "Divide two negative numbers", "Call divide(-20, -4)", "5",
                "value", new double[]{-20, -4}, 5));
        return list;
    }
}
