public class division {
    
    // Method to divide two numbers
    public static double divideTwoNumbers(double num1, double num2) {
        if (num2 == 0) {
            System.out.println("Error: Cannot divide by zero!");
            return 0;
        }
        return num1 / num2;
    }
    
    // Main method to demonstrate division
    public static void main(String[] args) {
        double number1 = 100;
        double number2 = 5;
        
        double result = divideTwoNumbers(number1, number2);
        
        System.out.println("Number 1: " + number1);
        System.out.println("Number 2: " + number2);
        System.out.println("Division Result: " + number1 + " / " + number2 + " = " + result);
    }
}
