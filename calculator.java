public class calculator {
    public static void main(String[] args) {
        int a = 10, b = 5;
        if (args.length >= 2) {
            try {
                a = Integer.parseInt(args[0]);
                b = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Both arguments must be integers. Using defaults 10 and 5.");
                a = 10;
                b = 5;
            }
        }
        System.out.println("Addition: " + (a+b));
        System.out.println("Multiplication: " + (a*b));
        System.out.println("Subtraction: " + (a-b));
    }
}