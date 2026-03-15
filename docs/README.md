# Simple Java Calculator

A basic command-line calculator written in Java that performs fundamental arithmetic operations (addition, subtraction, multiplication, and division) on two integers.

## Features

- **Basic Arithmetic Operations**: Addition, subtraction, multiplication, and division
- **Command-line Arguments**: Accepts two integers as command-line arguments
- **Default Values**: Uses default values (10 and 5) if insufficient or invalid arguments are provided
- **Error Handling**: Gracefully handles non-integer inputs with informative messages

## Requirements

- **Java Development Kit (JDK)** 8 or higher
- Basic command-line/terminal knowledge

## Building & Compilation

Since this is a single-file project with no external dependencies, compilation is straightforward:

```bash
# Compile the Java source file
javac calculator.java
```

This will generate a `calculator.class` file in the same directory.

## Usage

### Basic Usage with Arguments

```bash
java calculator <integer1> <integer2>
```

**Example:**
```bash
java calculator 15 3
```

**Output:**
```
Addition: 18
Multiplication: 45
Subtraction: 12
Division: 5.0
```

### Using Default Values

If you run the program without arguments or with invalid inputs:

```bash
java calculator
```

or

```bash
java calculator abc xyz
```

**Output:**
```
Both arguments must be integers. Using defaults 10 and 5.
Addition: 15
Multiplication: 50
Subtraction: 5
Division: 2.0
```

## Important Notes & Limitations

### Division Behavior
- Division uses floating-point arithmetic (`double`) to preserve decimal precision
- **Division by zero**: The program will output `Infinity` (if dividend ≠ 0) or `NaN` (if dividend = 0) rather than throwing an exception due to floating-point division

### Input Validation
- Only the first two command-line arguments are processed
- Non-integer arguments trigger fallback to default values with an error message
- No range validation (very large integers may cause overflow in multiplication)

## Security Considerations

1. **Input Validation**: The program validates command-line arguments but only for integer format. It does **not** validate against integer overflow/underflow conditions.

2. **No External Dependencies**: This project uses only Java standard library, minimizing supply-chain risk.

3. **Resource Usage**: Minimal memory footprint with no external network connections or file I/O.

4. **Division by Zero**: While handled gracefully via floating-point semantics, this produces mathematically undefined results (`Infinity`/`NaN`) rather than providing a user-friendly error message.

## Code Structure

```
calculator.java      # Single source file containing the main class
calculator.class     # Compiled bytecode (generated after compilation)
```

## Potential Improvements

For educational purposes, consider enhancing this calculator with:

- Integer overflow/underflow detection
- Custom division-by-zero handling with user feedback
- Support for more than two operands
- Interactive mode (prompting for input instead of command-line args)
- Additional operations (modulo, exponentiation)
- Unit tests
- Maven/Gradle build configuration
- Proper logging framework

## License

This educational code sample is provided as-is for learning purposes. Modify and use freely for personal and educational projects.

## Author

Created as a demonstration of basic Java programming concepts including:
- Command-line argument handling
- Exception handling (try-catch)
- Type conversion (string to integer)
- Basic arithmetic operations
- Simple program flow control

---

**Note**: This is intentionally minimal code for educational demonstration. For production use, add comprehensive error handling, input validation, and testing.