import java.util.Scanner;

/**
 * Simple CLI Calculator - menu-driven front end.
 * Version: matches Calculator.VERSION
 */
public class CalculatorApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            System.out.print("Choose an option (1-7): ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("7")) {
                System.out.println("Goodbye!");
                break;
            }

            try {
                if (choice.equals("6")) {
                    double a = readNumber(scanner, "Enter a number: ");
                    System.out.println("Result: " + Calculator.squareRoot(a));
                } else if (choice.matches("[1-5]")) {
                    double a = readNumber(scanner, "Enter first number: ");
                    double b = readNumber(scanner, "Enter second number: ");
                    switch (choice) {
                        case "1" -> System.out.println("Result: " + Calculator.formatNumber(Calculator.add(a, b)));
                        case "2" -> System.out.println("Result: " + Calculator.formatNumber(Calculator.subtract(a, b)));
                        case "3" -> System.out.println("Result: " + Calculator.formatNumber(Calculator.multiply(a, b)));
                        case "4" -> System.out.println("Result: " + Calculator.divide(a, b));
                        case "5" -> System.out.println("Result: " + Calculator.formatNumber(Calculator.power(a, b)));
                    }
                } else {
                    System.out.println("Invalid option. Please choose 1-7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter valid numeric input.");
            }
        }

        scanner.close();
    }

    private static double readNumber(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine().trim());
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("==== Simple Calculator (v" + Calculator.VERSION + ") ====");
        System.out.println("1. Add");
        System.out.println("2. Subtract");
        System.out.println("3. Multiply");
        System.out.println("4. Divide");
        System.out.println("5. Power (a ^ b)");
        System.out.println("6. Square Root");
        System.out.println("7. Exit");
        System.out.println("=======================================");
    }
}
