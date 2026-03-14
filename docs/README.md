# Simple Java Calculator

A lightweight command-line calculator written in Java that performs basic arithmetic operations (addition, multiplication, and subtraction) on two integers.

## Features
- Accepts two integer values as command-line arguments
- Falls back to default values (10 and 5) if:
  - Fewer than 2 arguments are provided
  - Arguments are non-integer values
- Displays results for:
  - Addition
  - Multiplication
  - Subtraction
- Simple error handling for invalid inputs

## Requirements
- Java Runtime Environment (JRE) 8+
- Java Development Kit (JDK) for compilation (optional if only running)

## Compilation
```bash
javac calculator.java
```

## Usage
```bash
java calculator [num1] [num2]
```

### Examples
**With valid arguments:**
```bash
java calculator 7 3
```
Output:
```
Addition: 10
Multiplication: 21
Subtraction: 4
```

**Without arguments (uses defaults):**
```bash
java calculator
```
Output:
```
Addition: 15
Multiplication: 50
Subtraction: 5
```

**With invalid arguments:**
```bash
java calculator abc 12
```
Output:
```
Both arguments must be integers. Using defaults 10 and 5.
Addition: 15
Multiplication: 50
Subtraction: 5
```

## Security Considerations

Based on the code analysis, the following security notes apply:

1. **Input Validation**: The program validates that command-line arguments are integers using `Integer.parseInt()` with exception handling. Invalid inputs fall back to safe default values.

2. **Integer Overflow**: The program uses `int` primitive type, which has a limited range (-2³¹ to 2³¹-1). For extremely large values (±2 billion), arithmetic operations may overflow and wrap around, producing unexpected results. This is a known limitation of primitive integer arithmetic in Java.

3. **Argument Handling**: Only the first two command-line arguments are used. Additional arguments are silently ignored, which could lead to unexpected behavior if users assume all arguments are processed.

4. **Error Information Disclosure**: Error messages are user-friendly but don't expose stack traces or internal details, which is good practice for production code.

5. **No External Dependencies**: The program is self-contained with no external libraries, minimizing supply chain risks.

## Limitations
- **No Division**: Deliberately excludes division to avoid division-by-zero scenarios
- **Integer-only**: Does not support floating-point numbers or other numeric types
- **No Negative Validation**: Accepts negative numbers without restriction (intentional for flexibility)
- **Class Naming**: The class name `calculator` (lowercase) violates Java naming conventions (should be `Calculator`), though this doesn't affect functionality

## Future Improvements
- Add overflow checking using `Math.addExact()`, `Math.multiplyExact()` to prevent silent overflows
- Support floating-point operations with proper validation
- Exit with non-zero status code on errors for better automation integration
- Add unit tests for boundary conditions and error cases

## License
This is a demonstration project. Modify and distribute as needed.

## Author
Created as a simple arithmetic utility example.