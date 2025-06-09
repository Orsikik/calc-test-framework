# 🧪 Calculator Test Framework

This project is an automated testing framework for validating the behavior and limitations of the `bc` command-line calculator.

It includes:
- ✅ A set of **unit tests** written in Java + TestNG
- 📊 **Allure reporting** for detailed test results
- 🐳 **Dockerized environment** for simple and consistent execution
- 🔁 **Local fallback mode** to run tests without Docker
- 📄 Supporting documentation (test plan, strategy, coverage)

---

## 📌 What This Project Tests

This test suite verifies:
- Arithmetic limits (integers, decimals, operations, nesting)
- Decimal behavior and normalization
- Runtime behavior and error handling (timeouts, invalid inputs)
- Edge cases and malformed expressions

The goal is to evaluate how well `bc` handles extreme input cases, rounding, error messages, and execution stability.

---

## 📂 Project Contents

- `README.md` — this file
- `test-plan.md` — full list of test cases and goals
- `test-strategy.md` — approach to testing and tooling
- `calc-test-framework-fat-1.0.jar` — runnable JAR with embedded test logic
- `start_bc_docker_tests.sh` — primary entry point (via Docker)
- `run_local.sh` — fallback local runner script (no Docker)
- `Dockerfile` — containerized test runner environment

---

## 🚀 How to Run

You can run the tests in two ways:
1. **Docker Mode** (recommended) — completely isolated, no setup required.
2. **Local Mode** — for environments where Docker is restricted or unavailable.


