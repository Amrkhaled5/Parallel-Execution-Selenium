# Parallel Execution Using ThreadLocal in Selenium

## Overview
This guide explains how to execute Selenium tests in parallel using **ThreadLocal** for thread-safe WebDriver management, based on the implementation in this project.

---

## Why Use ThreadLocal? 🤔

**ThreadLocal** is a Java mechanism that provides thread-isolated storage. Each thread gets its own independent copy of a variable.

### The Problem Without ThreadLocal:
When running tests in parallel, if multiple threads share the same WebDriver instance:
- **Thread 1** might be on the login page
- **Thread 2** tries to access the homepage using the same driver
- **Result**: Tests interfere with each other, causing failures and unpredictable behavior

### The Solution With ThreadLocal:
```java
private static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
```
- Each thread gets its own isolated WebDriver instance
- No conflicts between parallel test executions
- Thread-safe operations across all tests

---

## How ThreadLocal Works in Your Implementation

### 1. **ThreadLocal Declaration** (BaseTest.java)
```java
private static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
```
Creates a thread-safe container that holds a separate WebDriver for each thread.

### 2. **Setting the Driver for Each Thread**
```java
@BeforeClass
@Parameters("browser")
public void setUp(String browser) {
    if (browser.equalsIgnoreCase("chrome")) {
        driver = new ChromeDriver();
    } else if (browser.equalsIgnoreCase("edge")) {
        driver = new EdgeDriver();
    }
    
    threadDriver.set(driver);  // Store driver in ThreadLocal
    getDriver().manage().window().maximize();
    getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
}
```
- Creates a WebDriver instance based on browser parameter
- Stores it in ThreadLocal using `threadDriver.set(driver)`
- Each test thread gets its own driver instance

### 3. **Retrieving the Thread-Specific Driver**
```java
public WebDriver getDriver() {
    return threadDriver.get();  // Returns driver for current thread only
}
```
- `threadDriver.get()` returns the WebDriver instance belonging to the current thread
- Ensures each test uses only its own driver

### 4. **Cleanup After Tests**
```java
@AfterClass
public void tearDown() {
    if (getDriver() != null) {
        getDriver().quit();
        threadDriver.remove();  // Clean up ThreadLocal to prevent memory leaks
    }
}
```
- Quits the browser
- **Important**: `threadDriver.remove()` clears the ThreadLocal reference to prevent memory leaks

---

## TestNG Configuration (testng.xml)

```xml
<suite name="CrossBrowserSuite" parallel="tests" thread-count="2">
    <test name="ChromeTests">
        <parameter name="browser" value="chrome"/>
        <classes>
            <class name="tests.RegisterTest"/>
            <class name="tests.LoginTest"/>
        </classes>
    </test>

    <test name="EdgeTests">
        <parameter name="browser" value="edge"/>
        <classes>
            <class name="tests.RegisterTest"/>
            <class name="tests.LoginTest"/>
        </classes>
    </test>
</suite>
```

### Key Configurations:
- **`parallel="tests"`**: Runs different `<test>` blocks in parallel
- **`thread-count="2"`**: Maximum 2 threads run simultaneously
- **`<parameter name="browser" value="chrome"/>`**: Passes browser type to `@Parameters` in BaseTest

---

## How It All Works Together 🔄

1. **TestNG starts** with `thread-count="2"`, creating 2 threads
2. **Thread 1** executes `ChromeTests`:
   - Calls `setUp("chrome")` → Creates ChromeDriver
   - Stores it in ThreadLocal: `threadDriver.set(driver)`
   - Runs RegisterTest and LoginTest using `getDriver()`
   
3. **Thread 2** executes `EdgeTests` simultaneously:
   - Calls `setUp("edge")` → Creates EdgeDriver
   - Stores it in ThreadLocal: `threadDriver.set(driver)`
   - Runs RegisterTest and LoginTest using `getDriver()`

4. **Each thread operates independently**:
   - Thread 1's `getDriver()` → Returns ChromeDriver
   - Thread 2's `getDriver()` → Returns EdgeDriver
   - No conflicts or interference

5. **After tests complete**:
   - Each thread calls `tearDown()`
   - Quits its own driver
   - Removes ThreadLocal reference

---

## Execution Results ⚡

**Without Parallel Execution:**
- ChromeTests: 30 seconds
- EdgeTests: 30 seconds
- **Total: 60 seconds**

**With Parallel Execution (thread-count="2"):**
- ChromeTests and EdgeTests run simultaneously
- **Total: ~30 seconds**
- **Time Saved: 50%**

---

## Best Practices ✅

1. **Always use `getDriver()` instead of `driver` directly** in test classes
2. **Always call `threadDriver.remove()`** in tearDown to prevent memory leaks
3. Set `thread-count` based on system resources (typically 2-4 threads)
4. Ensure tests are independent with no shared state
5. Use `alwaysRun = true` in @AfterClass to guarantee cleanup

---

## Comparison: ThreadLocal vs DataProvider Approach

| Aspect | ThreadLocal | DataProvider |
|--------|-------------|--------------|
| **Use Case** | Cross-browser testing | Multiple test data scenarios |
| **Configuration** | testng.xml with `parallel="tests"` | `@DataProvider(parallel=true)` |
| **Driver Lifecycle** | One driver per `<test>` block | One driver per data iteration |
| **Thread Safety** | ThreadLocal ensures isolation | Driver created/destroyed per iteration |
| **Best For** | Running same tests on different browsers | Running same test with different inputs |

Both approaches achieve parallelization but serve different testing needs!

---

## Advantages ✅
- **Faster Execution**: Multiple browsers run tests simultaneously
- **Thread-Safe**: Each thread has isolated WebDriver instance
- **Cross-Browser Testing**: Easy to test on Chrome, Edge, Firefox, etc. in parallel
- **Clean Code**: Centralized driver management in BaseTest
- **Prevents Memory Leaks**: ThreadLocal cleanup ensures proper resource management

## Disadvantages ❌
- **Resource Intensive**: Multiple browser instances consume more memory/CPU
- **Complex Debugging**: Harder to trace issues when multiple tests fail
- **Requires Thread Safety**: All test code must be thread-safe
- **System Dependent**: Performance depends on available CPU cores and RAM
