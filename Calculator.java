/**
 * Simple CLI Calculator - core arithmetic logic.
 * Version: 1.0.0
 *
 * Supports: addition, subtraction, multiplication, division,
 * power, and square root.
 */
public class Calculator {

    public static final String VERSION = "1.1.0";

    public static double add(double a, double b) {
        return a + b;
    }

    public static double subtract(double a, double b) {
        return a - b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    /**
     * FIXED in 1.1.0: division by zero now returns a friendly error
     * message instead of silently producing "Infinity".
     */
    public static String divide(double a, double b) {
        if (b == 0) {
            return "Error: Cannot divide by zero";
        }
        return formatNumber(a / b);
    }

    public static double power(double a, double b) {
        return Math.pow(a, b);
    }

    /**
     * FIXED in 1.1.0: negative input now returns a friendly error
     * message instead of silently producing "NaN".
     */
    public static String squareRoot(double a) {
        if (a < 0) {
            return "Error: Cannot compute square root of a negative number";
        }
        return formatNumber(Math.sqrt(a));
    }

    /** Formats a double without a trailing ".0" for whole numbers. */
    public static String formatNumber(double value) {
        if (Double.isNaN(value)) {
            return "NaN";
        }
        if (Double.isInfinite(value)) {
            return value > 0 ? "Infinity" : "-Infinity";
        }
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            return String.valueOf((long) value);
        }
        // Round to 10 decimal places to avoid floating-point noise (e.g. 5.6000000000000005)
        double rounded = Math.round(value * 1e10) / 1e10;
        if (rounded == Math.floor(rounded)) {
            return String.valueOf((long) rounded);
        }
        return String.valueOf(rounded);
    }
}
