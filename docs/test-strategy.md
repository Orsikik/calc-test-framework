# 🧪 Test Strategy for basic calculators operations

## 🗂️ Overview
This document outlines the test strategy for validating basic arithmetic calculator tools. It describes the planned testing approach, test design techniques, value selection strategies (including boundary and equivalence partitions), and how we assess system limitations through structured input combinations.

---

## 🔍 Scope
- Limitation Discovery Phase
Each calculator tool is first tested to detect its operational and input/output boundaries using automated bash scripts. These include probing for expression complexity, numerical size, nesting, and formatting limits.
- Test Data Design Phase
Based on the discovered limits, we design precise test inputs (valid and invalid) targeting boundary and equivalence partitions. This ensures full coverage of expected behavior and failure conditions.
- Execution Phase (Unit Tests)
The generated test data is used in unit test scripts that verify correct behavior under both normal and extreme scenarios.

## ⚙️ Test Levels Covered
- **Unit Testing** — Validation of individual calculator expressions, operators, and edge behaviors.
- **System-Level Behavior Verification** — Observing full calculator behavior under combined, stress, and script-based inputs.

---
## 🛠️ Test Design Techniques Used
- **Equivalence Partitioning** — Partition input data into valid and invalid classes; test representative values from each.
- **Boundary Value Analysis** — Focus on values at, just below, and just above discovered thresholds.
