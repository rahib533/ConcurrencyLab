# Parallel Counter Increment in Java

This repository demonstrates and compares **race condition**, **`synchronized`**, **`AtomicInteger`**, and **`VarHandle`/CAS** (compare-and-set) approaches.  
Each example tries to increment a shared `counter` variable **1,000,000 times** using two threads.

> Goal: Observe correctness and behavior when the same task is implemented with different synchronization techniques.

---

## Table of Contents
- [Project Structure](#project-structure)
- [Examples Explained](#examples-explained)
  - [1) `IntWithRaceCondition`](#1-intwithracecondition)
  - [2) `IntWithSynchronized`](#2-intwithsynchronized)
  - [3) `IntWithAtomicInteger`](#3-intwithatomicinteger)
  - [4) `IntWithCustomAtomicInteger` (VarHandle + CAS)](#4-intwithcustomatomicinteger-varhandle--cas)
- [Running](#running)
- [Expected Output](#expected-output)
- [Comparison Table](#comparison-table)
- [Technical Notes](#technical-notes)
- [Performance and Further Testing](#performance-and-further-testing)
- [License](#license)

---

## Project Structure

```
com/rahibjafarov/
├── IntWithAtomicInteger.java
├── IntWithCustomAtomicInteger.java
├── IntWithRaceCondition.java
├── IntWithSynchronized.java
└── Main.java
```

---

## Examples Explained

### 1) `IntWithRaceCondition`
**Demonstrates the problem**: `volatile int counter` with `counter++` is not atomic.  
When two threads read and write concurrently, increments are lost, and the final result is **less than 2,000,000**.

Key points:
- `volatile` ensures **visibility**, not **atomicity**.
- `counter++` is a `read-modify-write` sequence → leads to race condition.

---

### 2) `IntWithSynchronized`
**Solution 1**: The `increment()` method is protected with `synchronized`; only one thread can enter at a time.

Key points:
- Produces the correct result (2,000,000).
- May introduce contention and slower performance due to monitor locks.

---

### 3) `IntWithAtomicInteger`
**Solution 2**: `AtomicInteger` provides atomic increment via CAS (compare-and-set): `incrementAndGet()`.

Key points:
- Lock-free approach.
- Typically more efficient than `synchronized` under high concurrency.

---

### 4) `IntWithCustomAtomicInteger` (VarHandle + CAS)
**Solution 3**: Uses `VarHandle` for `getVolatile` and CAS (`compareAndSet`) in a retry loop.

Key points:
- Custom CAS loop, similar in principle to `AtomicInteger`.
- Lower-level control, useful for learning and experimentation.

---

## Running

### Without Maven/Gradle
1. Install Java 11+.
2. Keep files under the correct package structure (`com/rahibjafarov`).
3. Compile:
   ```bash
   javac com/rahibjafarov/*.java
   ```
4. Run:
   ```bash
   java com.rahibjafarov.Main
   ```

### With Maven (optional)
Place files under `src/main/java` and run as a standard Maven project.

---

## Expected Output

`Main` runs all four classes sequentially and prints their results:

```
com.rahibjafarov.IntWithRaceCondition
Counter: <usually less than 2,000,000>

com.rahibjafarov.IntWithSynchronized
Counter: 2000000

com.rahibjafarov.IntWithAtomicInteger
Counter: 2000000

com.rahibjafarov.IntWithCustomAtomicInteger
Counter: 2000000
```

> Note: Output may vary slightly depending on the system and JVM, but the logic remains the same.

---

## Comparison Table

| Approach | Correctness | Typical Behavior Under Concurrency | Notes |
|---|---|---|---|
| `volatile` + `counter++` | **Incorrect** | Race condition, lost updates | Not atomic; visibility only |
| `synchronized` | **Correct** | Possible contention | Monitor lock ensures mutual exclusion |
| `AtomicInteger` | **Correct** | High throughput | Lock-free with CAS |
| `VarHandle` + CAS | **Correct** | Similar to `AtomicInteger` | Low-level, educational example |

---

## Technical Notes
- **`volatile`**: Ensures read/write visibility; does not guarantee atomicity.
- **`counter++`**: Three steps (read, increment, write) → race condition in concurrent execution.
- **`synchronized`**: Acquires monitor lock at entry, releases at exit; protects critical section.
- **CAS (compare-and-set)**: "If current value equals expected, write new value" → lock-free synchronization.
- **`VarHandle`**: Java 9+ API for low-level, analyzable field access (`getVolatile`, `compareAndSet`, etc.).

---

## Performance and Further Testing
- Increase thread count (e.g., 4, 8, 16) to observe behavior of each approach.
- JVM flags (`-Xms`, `-Xmx`) won’t directly affect correctness but can help with stability in benchmarks.
- For microbenchmarks, use **JMH** (Java Microbenchmark Harness):
  - For small critical sections, `AtomicInteger` is usually faster than `synchronized`.
  - Under low contention, the difference may be minimal.

**Tip:** Parameterize iteration count and thread count in the runnable to make testing easier.

---

## License

This project is licensed under the MIT License — see the [LICENSE](./LICENSE) file for details.
