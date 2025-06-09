## 🧪 Test Case Groups

## ✅ 1. Arithmetic Operation Input/Output Limits — Unit Tests
*Covers numeric boundaries, expression size, nesting, and precision limits in line with official `bc` specifications.*

This group includes tests for:
- maximum single integer length
- decimal scale boundaries
- number of operations and operands per expression
- deep nesting
- large-scale multiplication
It also checks how `bc` handles inputs that exceed internal limits or trigger timeouts or truncations.
---

## 🧪 2. Execution Behavior & Error Handling — Unit Tests
*Validates how `bc` handles syntactic and runtime errors, malformed input, and boundary edge cases.*

This group consolidates:
- runtime behavior (e.g. execution timeouts, undefined variables)
- syntax error handling (e.g. missing operators, unterminated parentheses)
- response to empty or mixed-validity input
- general robustness during batch or complex expression processing
The goal is to confirm graceful failure and stability under invalid or unexpected input.
---

## ✅ 3. Arithmetic & Decimal Behavior — Final Selection
*Ensures correctness of calculation logic, operator precedence, decimal normalization, and precision.*

Tests in this group confirm:
- correct decimal arithmetic
- proper scale application
- operator precedence rules
- expression evaluation across mixed operators and grouped terms
These ensure `bc` produces accurate and predictable results for standard usage scenarios.
---

### ✅ Arithmetic Operation Input/Output Limits — Unit Tests (based on official `bc` limitations)

| № | Description                                  | Example                                             | Expected Result                                | Type     |
|---|----------------------------------------------|-----------------------------------------------------|------------------------------------------------|----------|
| 1 | Integer within limit                         | `999...999 + 1` (3000-digit integer)                | Correct result returned                        | Positive |
| 2 | Integer exceeds limit                        | `999...999 + 1` (25,000+ digits)                    | Error or silent overflow (`INT_MAX` behavior)  | Negative |
| 3 | Decimal scale within limit                   | `scale=5000; 1/3`                                   | Accurate result with 5000 decimals             | Positive |
| 4 | Decimal scale exceeds limit                  | `scale=450000000; 1/3`                              | Timeout or truncated result                    | Negative |
| 5 | 1000 operations                              | `1+1+1+...` (1000 times)                            | Correct sum calculated                         | Positive |
| 6 | 10,000+ operations                           | `1+1+1+...` (10,000+ times)                         | Parser error or timeout                        | Negative |
| 7 | Large multiplication                         | `999...999 * 999...999` (2000-digit operands)       | Correct long result                            | Positive |
| 8 | Too-large multiplication                     | `999...999 * 999...999` (100,000-digit operands)    | Error, truncation, or incorrect result         | Negative |
| 9 | Valid nesting of parentheses                 | `((1+2)*(3+(4/5)))`                                 | Correct result returned                        | Positive |
---

### 🧪 Execution Behavior & Error Handling — Unit Tests (final selection)

| №  | Description                                 | Example                          | Expected Result                                     | Type        |
|----|---------------------------------------------|----------------------------------|-----------------------------------------------------|-------------|
| 11 | Empty input                                 | *(no input)*                     | Graceful no-op or message                           | Edge Case   |
| 12 | Invalid syntax                              | `1 + * 2`                        | Parser error, descriptive message                   | Negative    |
| 13 | Division by zero                            | `1 / 0`                          | Runtime error or specific message                   | Negative    |
| 14 | Undefined variable                          | `a + 1`                          | Error about undefined symbol                        | Negative    |
| 15 | Unterminated parentheses                    | `(1 + (2 + 3)`                   | Syntax or parser error                              | Negative    |
| 16 | Mixed valid and invalid lines               | `1+1\nabc\n2+2`                  | Valid lines processed; errors reported separately   | Robustness  |
| 17 | Expression ending with operator             | `5 +`                            | Syntax error or request for missing operand         | Negative    |
| 18 | Misplaced decimal point                     | `. + 1`                          | Syntax error                                        | Negative    |
| 19 | Malformed scale assignment                  | `scale=abc`                      | Error or fallback to default                        | Negative    |

### ✅ Arithmetic & Decimal Behavior — Final Selection

| №   | Description                             | Example                          | Expected Result                                | Type       |
|-----|-----------------------------------------|----------------------------------|------------------------------------------------|------------|
| 20  | Basic decimal addition                  | `1.5 + 2.25`                     | `3.75`                                         | Positive   |
| 21  | Decimal multiplication with scale       | `scale=4; 2.25 * 3.1`            | `6.9750`                                       | Positive   |
| 22  | High-precision division                 | `scale=50; 1 / 7`                | 50 decimal digits                              | Edge Case  |
| 23  | Decimal normalization                   | `scale=5; 1.2 + 0`               | `1.20000`                                      | Positive   |
| 24  | Division with decimal result            | `10 / 4`                         | `2.5` (or truncated depending on scale)        | Positive   |
| 25  | Mixed operations                        | `2 + 3 * 4 - 5`                  | `9` (operator precedence)                      | Positive   |
| 26  | Parentheses affecting order             | `(2 + 3) * (4 - 5)`              | `-5`                                           | Positive   |
| 27  | Full expression evaluation              | `5 + 6 - 3 * 2 / 1`              | `5`                                            | Positive   |