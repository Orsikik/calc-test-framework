## 🧪 Test Case Groups

### 1. Arithmetic Operation Limits
- Single integer length
- Single decimal length
- Max operations in expression
- Max operands in expression
- Multiplication result length

### 2. Input Handling
- Max input line length
- Max total input size (script)
- Expression nesting depth
- Max number of expressions per run

### 3. Output Handling
- Max supported scale
- Multiplication output overflow
- Truncation or formatting issues

### 4. Execution Behavior
- Expression execution timeout
- Batch execution stability
- Combined expression complexity

### 5. Tool-Specific Cases (if applicable)
- Named variables/functions
- Tool-specific configs (scale, precision, etc.)

### 6. Negative & Edge Cases
- Empty input
- Malformed expressions
- Unsupported syntax


### ✅ Arithmetic Operation Limits — Unit Tests

| №  | Test Case Description                                  | Input Example                         | Expected Behavior                                       | Type      |
|----|--------------------------------------------------------|---------------------------------------|---------------------------------------------------------|-----------|
| 1  | Valid single integer length (within limit)             | `999...999 + 1` (e.g., 3000 digits)   | Correct result returned                                 | Positive  |
| 2  | Exceeding single integer length                        | `999...9999 + 1` (beyond limit)       | Error or overflow                                       | Negative  |
| 3  | Valid decimal digits after point (within scale)        | `scale=5000; 1/3`                     | Accurate result with 5000 decimals                      | Positive  |
| 4  | Exceeding decimal scale                                | `scale=20000; 1/3`                    | Error or silent truncation                              | Negative  |
| 5  | Valid number of operations in expression               | `1+1+1+...` (1000 operations)         | Correct sum calculated                                  | Positive  |
| 6  | Too many operations in expression                      | `1+1+1+...` (10000+ operations)       | Parser error or timeout                                 | Negative  |
| 7  | Valid number of operands                               | `1+1+1+...` (up to operand limit)     | Correct sum calculated                                  | Positive  |
| 8  | Excessive operand count                                | `1+1+1+...` (beyond operand limit)    | Error or incorrect result                               | Negative  |
| 9  | Valid multiplication of large numbers                  | `99999...9 * 99999...9`               | Correct long result returned                            | Positive  |
| 10 | Result too large from multiplication                   | Very long operands → 6000+ digit res | Error, truncation, or overflow                           | Negative  |

### 📥 Input Handling — Unit Tests

| №  | Test Case Description                                      | Input Example                          | Expected Behavior                                       | Type      |
|----|------------------------------------------------------------|----------------------------------------|---------------------------------------------------------|-----------|
| 11 | Valid number of expressions per run                        | `1+1\n2+2\n3+3\n...` (e.g., 1000 lines)| All expressions evaluated successfully                  | Positive  |
| 12 | Too many expressions in a single run                       | `1+1\n1+1\n...` (10000+ lines)         | Error, crash, or memory overflow                        | Negative  |
| 13 | Valid input line length                                    | `999+999+999+...` (short enough line)  | Expression parsed and evaluated correctly               | Positive  |
| 14 | Excessive input line length                                | Long unbroken line with 10000+ chars   | Buffer overflow, parse error, or crash                  | Negative  |
| 15 | Valid script input length (multiline input)                | Script with 1000 short lines           | All lines processed successfully                        | Positive  |
| 16 | Oversized script input                                     | Script with 50000+ lines               | Rejected or causes memory issue                         | Negative  |
| 17 | Valid nesting of parentheses                               | `((1+2)*(3+(4/5)))`                    | Correct result returned                                 | Positive  |
| 18 | Excessive nesting of parentheses                           | `((((((((1+1))))))))` (too deep)       | Stack overflow, parse error, or crash                   | Negative  |

### 📤 Output Handling — Unit Tests

| №  | Test Case Description                                 | Input Example                                        | Expected Behavior                                           | Type      |
|----|-------------------------------------------------------|------------------------------------------------------|-------------------------------------------------------------|-----------|
| 19 | Valid multiplication result length                    | `999...999 * 999...999` (e.g., 500 digits × 500)     | Full result returned correctly                              | Positive  |
| 20 | Multiplication result exceeds supported length        | `999...999 * 999...999` (e.g., 3000 digits × 3000)   | Result truncated, overflow error, or crash                  | Negative  |
| 21 | Valid scale in division                               | `scale=100; 1/3`                                     | Returns value with 100 decimal places                       | Positive  |
| 22 | Excessive scale in division                           | `scale=10000; 1/3`                                   | Truncated output, error message, or internal limit reached  | Negative  |
| 23 | Output formatting with trailing zeros                 | `scale=5; 1/2`                                       | Result formatted as `0.50000`                               | Positive  |
| 24 | Handling of very long numeric output                  | Expression that produces 10,000+ character result    | Either truncated output or handled without issue            | Negative  |

### ⚙️ Execution Behavior — Unit Tests

| №  | Test Case Description                               | Input Example                                     | Expected Behavior                                           | Type      |
|----|-----------------------------------------------------|---------------------------------------------------|-------------------------------------------------------------|-----------|
| 25 | Valid script with moderate length                   | 100 expressions like `1+1`, separated by newlines | Executes all successfully                                   | Positive  |
| 26 | Exceeding max expressions per execution             | Thousands of expressions in one input script      | Crash, error, or partial processing                         | Negative  |
| 27 | Valid line length                                   | Expression with ~100 characters                   | Executes correctly                                          | Positive  |
| 28 | Exceeding maximum input line length                 | Single line with 10,000+ characters               | Error, buffer overflow, or crash                            | Negative  |
| 29 | Empty input                                         | *(no input at all)*                               | No-op, graceful termination, or relevant message            | Edge Case |
| 30 | Invalid syntax                                      | `1 + * 2`                                         | Parser error, descriptive message                           | Negative  |
| 31 | Unexpected input characters                         | `1 + abc`                                         | Error or graceful handling                                  | Negative  |
| 32 | Combination stress test                             | Deep nesting, high scale, long numbers            | Executes correctly or crashes depending on limits           | Stress    |

### 🧩 Error Handling — Unit Tests

| №  | Test Case Description                             | Input Example                         | Expected Behavior                                | Type      |
|----|---------------------------------------------------|---------------------------------------|--------------------------------------------------|-----------|
| 33 | Division by zero                                  | `1 / 0`                               | Error or special message                         | Negative  |
| 34 | Undefined variable                                | `a + 1`                               | Error about undefined symbol                     | Negative  |
| 35 | Invalid function or operator                      | `foo(2)` or `2 $ 3`                   | Error message about invalid usage                | Negative  |
| 36 | Unterminated parentheses                          | `(1 + (2 + 3)`                        | Parser error indicating syntax issue             | Negative  |
| 37 | Excessive decimal precision request               | `scale=999999; 1/3`                   | Either truncate, error, or timeout               | Edge Case |
| 38 | Multiple errors in one script                     | `1/0\n2+*3\na+1`                      | Each error reported or first stops execution     | Negative  |
| 39 | Mixed valid and invalid lines                     | `1+1\nabc\n2+2`                       | Valid lines processed; errors handled separately | Robustness|
| 40 | Handling unexpected EOF                           | Script ends mid-expression            | Error or graceful exit                           | Edge Case |

### 🔢 Decimal and Scale Behavior — Unit Tests

| №  | Test Case Description                                  | Input Example                               | Expected Behavior                                | Type      |
|----|--------------------------------------------------------|---------------------------------------------|--------------------------------------------------|-----------|
| 41 | Basic decimal addition                                 | `1.5 + 2.25`                                | `3.75`                                           | Positive  |
| 42 | Decimal subtraction with precision                     | `5.00 - 1.2345`                             | `3.7655` or truncated based on scale             | Positive  |
| 43 | Multiplication with decimals and scale                 | `scale=4; 2.25 * 3.1`                       | `6.9750`                                         | Positive  |
| 44 | Division result respecting default scale               | `1 / 3`                                     | Limited decimal places (tool-specific)           | Positive  |
| 45 | Division with explicit high scale                      | `scale=50; 1 / 7`                           | Long result with 50 decimal digits               | Edge Case |
| 46 | Zero-padding and decimal normalization                 | `scale=5; 1.2 + 0`                          | `1.20000`                                        | Positive  |
| 47 | Negative decimal calculations                          | `-1.75 + 2.25`                              | `0.5`                                            | Positive  |
| 48 | Edge case: high scale, long decimals                   | `scale=9999; 1/3`                           | Tool should not crash                            | Edge Case |


### 🧮 Calculation Accuracy — Unit Tests

| №  | Test Case Description                                  | Input Example                               | Expected Behavior                                | Type      |
|----|--------------------------------------------------------|---------------------------------------------|--------------------------------------------------|-----------|
| 49 | Addition with large integers                           | `999999999 + 888888888`                     | `1888888887`                                     | Positive  |
| 50 | Subtraction resulting in zero                          | `12345 - 12345`                             | `0`                                              | Positive  |
| 51 | Multiplication with zero                               | `999999 * 0`                                | `0`                                              | Positive  |
| 52 | Multiplication of large integers                       | `999999 * 888888`                           | Correct large result                             | Positive  |
| 53 | Division resulting in integer                          | `144 / 12`                                  | `12`                                             | Positive  |
| 54 | Division resulting in decimal                          | `10 / 4`                                    | `2.5` or truncated/rounded                       | Positive  |
| 55 | Calculation using mixed operations                     | `2 + 3 * 4 - 5`                             | `9` (respecting order of operations)             | Positive  |
| 56 | Expression with parentheses affecting order            | `(2 + 3) * (4 - 5)`                         | `-5`                                             | Positive  |
| 57 | Repeated operations with same value                    | `10 - 2 - 2 - 2`                            | `4`                                              | Positive  |
| 58 | Expression combining all operations                    | `5 + 6 - 3 * 2 / 1`                         | `5 + 6 - 6 = 5`                                  | Positive  |


### 📉 Error Handling & Stability — Unit Tests
| №  | Test Case Description                                    | Input Example                          | Expected Behavior                             | Type      |
|----|----------------------------------------------------------|----------------------------------------|-----------------------------------------------|-----------|
| 59 | Division by zero                                         | `1 / 0`                                | Returns error or specific message             | Negative  |
| 60 | Invalid characters in expression                         | `2 + abc`                              | Error about invalid syntax                    | Negative  |
| 61 | Unclosed parentheses                                     | `(1 + 2`                               | Syntax error                                  | Negative  |
| 62 | Empty expression                                         | *(empty input)*                        | Graceful no-op or clear error                 | Negative  |
| 63 | Expression ending with operator                          | `5 +`                                  | Error or prompt for missing operand           | Negative  |
| 64 | Unsupported operation or function                        | `sqrt(4)` (if not supported)           | Error indicating unknown function             | Negative  |
| 65 | Misplaced decimal point                                  | `. + 1`                                | Syntax error                                  | Negative  |
| 66 | Too many consecutive operators                           | `1 ++ 2`                               | Error or rejection                            | Negative  |
| 67 | Input with tab or non-breaking space                     | `1[tab]+[nbsp]2`                       | Handled or clean error                        | Negative  |
| 68 | Expression with non-UTF8 characters (if tool allows)     | Binary garbage input                   | Clean error or rejection                      | Negative  |
| 69 | Malformed scale setting                                  | `scale=abc`                            | Error or fallback to default scale            | Negative  |
| 70 | Runtime freeze under heavy load (timeout test)           | Huge expression with 50k operations    | Terminated or handled within timeout          | Stability |
| 71 | Memory overflow simulation                               | Massive input script (~10MB)           | Either graceful fail or controlled behavior   | Stability |