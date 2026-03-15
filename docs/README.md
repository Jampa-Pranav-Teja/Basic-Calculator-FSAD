# Simple Java Calculator

A lightweight command-line calculator implemented in Java that performs basic arithmetic operations on two integers.

## Features

- **Basic Arithmetic Operations**:
  - Addition
  - Subtraction
  - Multiplication
- **Flexible Input**: Accepts command-line arguments or uses default values (10 and 5)
- **Robust Error Handling**: Gracefully handles non-integer inputs with clear error messages
- **Zero Dependencies**: Pure Java implementation with no external libraries

## Security Analysis

This implementation has been reviewed for common security vulnerabilities with the following findings:

### ✅ **Security Strengths**
- **No Command Injection**: The application only performs arithmetic operations on parsed integers and never executes shell commands or external processes.
- **Input Validation**: Uses `Integer.parseInt()` with exception handling to prevent malformed input from causing crashes.
- **Resource Safe**: No file operations, network connections, or database access that could lead to resource leaks or injection attacks.
- **Deterministic Behavior**: Operations are pure mathematical calculations with no side effects.

### ⚠️ **Considerations**
- **Integer Overflow/Underflow**: The implementation does not check for integer overflow/underflow. For production use with untrusted inputs, consider using `Math.addExact()`, `Math.multiplyExact()`, etc., which throw `ArithmeticException` on overflow.
- **Default Value Behavior**: Invalid inputs fall back to hardcoded defaults (10 and 5). Ensure this behavior aligns with your requirements for error handling.
- **No Input Sanitization Needed**: Since inputs are strictly parsed as integers and no string operations are performed on them, traditional sanitization isn't applicable.

## Usage

### Compilation
```bash
javac calculator.java
```

### Execution

**With custom arguments** (two integers):
```bash
java calculator 15 3
```
Output:
```
Addition: 18
Multiplication: 45
Subtraction: 12
```

**Without arguments** (uses defaults: 10 and 5):
```bash
java calculator
```
Output:
```
Addition: 15
Multiplication: 50
Subtraction: 5
```

**With invalid arguments** (handles non-integers gracefully):
```bash
java calculator abc 20
```
Output:
```
Both arguments must be integers. Using defaults 10 and 5.
Addition: 15
Multiplication: 50
Subtraction: 5
```

## Code Structure

- **Single Class Design**: All functionality contained in `calculator` class
- **Main Method**: Entry point handling argument parsing and operation execution
- **Error Handling**: Try-catch block for `NumberFormatException` with fallback to defaults

## Requirements

- Java 8 or higher
- No external dependencies

## Future Enhancements

For production deployment, consider:
1. Adding overflow/underflow checks using `Math.*Exact()` methods
2. Supporting more operations (division, modulo) with zero-division checks
3. Configurable default values via properties file or environment variables
4. Input range validation based on business requirements
5. Unit tests for edge cases and error handling

## License

This is a demonstration project. Adapt as needed for your use case.