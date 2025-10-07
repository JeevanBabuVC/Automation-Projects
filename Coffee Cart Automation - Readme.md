## Project Overview:

* This document provides a comprehensive analysis of each class in the Coffee Cart automation framework,
explaining the purpose, methods, design principles, and improvement opportunities.
* The analysis is written from a practical perspective to help developers understand the implementation decisions and architectural choices.
* This file has the details about the journey and how user can execute the test.

## Goal:

* The goal is to build a robust Selenium automation framework that thoroughly tests our coffee cart e-commerce platform using clean Page Object Model architecture and data-driven TestNG scenarios. 
* Create comprehensive test coverage from menu browsing to checkout completion, with detailed Allure reporting and cross-browser compatibility to ensure a flawless customer experience.

**Before starting the execution part let's understand the project structure summary**

## Required Software:

* Java 24 (or compatible JDK)
* Maven 3.6+
* Chrome browser (latest)
* Firefox browser (latest)
* **UI - https://coffee-cart.app/**

## Verification:

* java -version
* mvn -version
* google-chrome --version
* firefox --version

## Planning:
I used linux commands to create the directories, below are the details of it.

### For Project Root and Maven architecture:
```
* mkdir -p CoffeeCart-Automation/{src/{main,test}/java/com/coffeecart,resources,test-output,target/allure-results}
* cd CoffeeCart-Automation
```
### To create package directories:
```
* mkdir -p src/main/java/com/coffeecart/{managers,utils,data}
* mkdir -p src/test/java/com/coffeecart/{tests,listeners}
```
### Core configuration file:
```
* touch pom.xml
* touch resources/allure.properties
* touch resources/config.properties  
```
### Core Framework:

#### Utilities and managers:
```
* touch src/main/java/com/coffeecart/utils/ConfigReader.java
* touch src/main/java/com/coffeecart/utils/LoggerUtil.java
* touch src/main/java/com/coffeecart/managers/ConfigManager.java
* touch src/main/java/com/coffeecart/managers/BrowserFactory.java
* touch src/main/java/com/coffeecart/utils/ScreenshotUtil.java
* touch src/main/java/com/coffeecart/data/TestDataProvider.java
```

#### Test infrastructure:
```
* touch src/test/java/com/coffeecart/tests/BaseTest.java
* touch src/test/java/com/coffeecart/listeners/TestListener.java
```

### Test Classes:

```
* touch src/test/java/com/coffeecart/tests/MenuTests.java
* touch src/test/java/com/coffeecart/tests/CheckoutTests.java  
* touch src/test/java/com/coffeecart/tests/EndToEndTests.java
* touch src/test/java/com/coffeecart/tests/CartTests.java  

```

### TestNG Configuration:

```
* touch testng-optimized.xml
* touch testng-chrome-only.xml
* touch testng-firefox-only.xml

```


### Basic Application for making it easy to use:

* `touch src/main/java/com/coffeecart/CoffeeCartApp.java  `

## Usage details of each class files:

### `TestDataProvider` : Manages test data from external sources (Excel files, JSON, etc.)

* Reads test data from Excel files or other sources.
* Provides data to TestNG tests using @DataProvider annotations.
* Handles different types of test data (user info, product details, payment data).

#### Methods in TestDataProvider:

* fetchCoffeeData() – Returns coffee items with their quantities and prices for menu testing.
* fetchUserDetails() – Provides sample usernames and email addresses for checkout form testing.
* fetchCsvUserData() – Supplies additional test users simulating CSV data for validation.
* fetchExcelCoffeeData() – Loads coffee data from an Excel sheet for advanced test scenarios.
* fetchExcelUserData() – Retrieves user data from Excel for comprehensive user testing.
* fetchTestDataFromExcel(String sheetName) – Converts Excel data into a List<Map<String, String>> format for more complex data handling.

#### Uses:

* TestNG tests call these methods with @Test(dataProvider = "getUserData")
* Separates test data from test logic.
* Makes tests data-driven and reusable.

### Managers Directory:

* The managers directory contains three key classes that handle the core infrastructure of the Coffee Cart automation framework:

**BrowserFactory.java** - Used for browser creation.

Sets up Chrome and Firefox browsers with custom configurations
Handles headless mode 
Uses WebDriverManager for automatic driver setup
Thread-safe driver management with proper cleanup

**ConfigManager.java** -  configuration management

* Loads all settings from properties files 
* Provides easy access to timeouts, URLs, and test data
* Supports system property overrides for flexible test execution
* Contains  of getter methods for specific test configurations like wait times, selectors, and test data

* **DriverManager.java** - The WebDriver orchestrator

* Thread-safe WebDriver instance management
* Works with BrowserFactory to create drivers
* Provides simple static methods for getting and closing drivers

### Utils Directory:

The utils directory contains five essential utility classes that provide common functionality across the Coffee Cart test framework:

**ConfigReader.java** - The basic configuration reader

* Simple properties file loader with static initialization
* Provides typed getters for string, int, and boolean values
* Basic error handling for missing properties

* **ExcelReader.java** - The CSV data handler

* Reads test data from CSV files
* Supports multiple data formats (UserData, CoffeeData, TestConstants)
* Converts CSV data to Object arrays for TestNG data providers
* Also provides map-based data access for flexible test scenarios

**ExcelUtil.java** - CSV utility

* More advanced CSV reading with test case name lookup
* Returns data as key-value maps for specific test cases
* Handles sheet-based organization within CSV files
* Designed for data-driven testing with named test scenarios

**LoggerUtil.java** -  centralized logging 

* Wraps SLF4J logger for consistent logging across the framework
* Provides standard log levels (info, error, warn, debug)
* Includes overloaded error method for exception logging
* Single point of control for all framework logging

**ScreenshotUtil.java** - Capture test failure screenshots

* Captures timestamped screenshots for failed tests
* Creates screenshot directories automatically
* Uses configurable screenshot paths from ConfigManager
* Returns screenshot file paths for test reporting integration

### Pages Directory Summary
The pages directory contains four-page object classes that represent the main sections of the Coffee Cart application using the Page Object Model pattern:

**BasePage.java** - The foundation class

* Provides common WebDriver operations for all page objects
* Handles element waiting strategies (visibility, clickability)
* Sets up WebDriverWait and PageFactory initialization
* Base class that all other pages extend

**MenuPage.java** - The main coffee menu handler (largest and most complex)

* Manages all coffee menu interactions and item selection
* Handles promotional offers and special deals
* Provides cart operations (adding items, quantity management)
* Includes price validation and ingredient checking
* Manages cart preview popup and hover interactions
* Contains extensive validation methods for menu items, prices, and ingredients

**CartPage.java** - The shopping cart manager

* Manages cart navigation and item display
* Checks for empty cart states and promotional items
* Provides cart update waiting mechanisms
* Validates discounted items and cart totals

**CheckoutPage.java** - The payment form controller

* Handles customer information entry (name, email)
* Provides form validation with email format checking
* Manages promotional checkbox interactions
* Handles order submission and form state validation
* Includes comprehensive form validation methods

### Listeners Directory

**TestListener.java** - The test execution monitor

* Implements ITestListener for comprehensive test lifecycle management
* Automatically captures screenshots on test failures for debugging
* Provides detailed logging for test start, success, failure, and skip events
* Calculates test execution duration and provides execution summaries
* Integrates with Allure reporting for enhanced test reports
* Handles test suite statistics (total, passed, failed, skipped tests)

### Suites Directory

**TestNGRunner.java** - The programmatic test executor

* Provides Java-based TestNG execution instead of command-line running
* Configures test suites from XML files programmatically
* Allows flexible test execution control from IDE or CI/CD pipelines

### Tests Directory

**BaseTest.java** - The foundation test class

* Provides common setup and teardown for all test classes
* Handles browser initialization with retry logic for navigation failures
* Manages WebDriver lifecycle with proper resource cleanup
* Supports parameterized browser testing (Chrome, Firefox)
* Implements thread-safe driver management
* Provides navigation retry mechanisms for unstable environments

**MenuTests.java** - The coffee menu test suite (most comprehensive)

* Tests menu page loading and element visibility
* Validates item arrangement, pricing, and display formatting
* Includes boundary testing for price ranges (valid and invalid scenarios)
* Tests ingredient validation and item categorization
* Handles promotional offers and special deals testing
* Uses data providers for parameterized testing with coffee items
* Includes cart interaction and reset functionality
* Browser-specific handling for different interaction behaviors

**CartTests.java** - The shopping cart test suite

* Tests empty cart states and navigation
* Validates single and multiple item additions
* Includes cart total calculation verification
* Tests promotional workflows with discounted items
* Provides quantity control validation

**CheckoutTests.java** - The payment form test suite

* Tests checkout form display and initial state validation
* Handles customer detail entry and form field validation
* Includes email format validation (valid/invalid scenarios)
* Tests promotional offer toggle functionality
* Validates form submission and order placement
* Uses both hardcoded and CSV data provider testing
* Implements proper form cleanup between tests

**EndToEndTests.java** - The complete workflow test suite

* Tests full user journey from menu to order completion
* Validates complete ordering workflow integration
* Includes cross-page navigation and state management
* Tests data flow between different application sections
* Provides comprehensive workflow validation

### Test Case Summary Table

#### Both Chrome and Firefox:
| Test Class     | Base Tests | Data Provider Tests | Total | Pass | Failures (Intended) |
|----------------|------------|-------------------|-------|------|---------------------|
| Menu Tests     | 17 | 6 (3×2 data sets) | 23 | 21 | 2 |
| Cart Tests     | 7 | 0 | 7 | 6 | 1 |
| Checkout Tests | 9 | 5 (2+3 data sets) | 14 | 14 | 0 |
| EndToEnd Tests | 1 | 0 | 1 | 1 | 0 |
| Total          | 34 | 11 | 45 | 42 | 3 |

#### Only One Browser:
| Test Class    | Base Tests | Data Provider Tests | Total | Pass | Failures (Intended) |
|---------------|------------|-------------------|-------|------|---------------------|
| MenuTests     | 17 | 6 (3×2 data sets) | 23 | 21 | 2 |
| CartTests     | 7 | 0 | 7 | 7 | 0 |
| CheckoutTests | 9 | 5 (2+3 data sets) | 14 | 14 | 0 |
| EndToEndTests | 1 | 0 | 1 | 1 | 0 |
| Total         | 34 | 11 | 45 | 43 | 2 |

### Intended Failures Breakdown:

**MenuTests** (2 intended failures):
* `testItemsBetweenTwentyAndThirty()` - Tests invalid price range 20−30 (Expected to fail)
* `testItemsAboveThirty()` - Tests items above $30 threshold (Expected to fail)

**CartTests** (1 browser-specific failure): [In headless mode I faced this issue]
* `testCartPreview()` - Firefox hover limitation in headless mode (Expected to fail in Firefox only)

### Data Provider Test Counts:
* **coffeeData**: 3 data sets (Espresso, Espresso con Panna, Flat White) × 2 test methods = 6 tests
* **userDetails**: 2 data sets × 1 test method = 2 tests
* **csvUserData**: 3 data sets × 1 test method = 3 tests

## Design Patterns Implementation Analysis

### 1. Page Object Model (POM) Pattern
**Classes**: BasePage, MenuPage, CartPage, CheckoutPage

**Implementation:**
* BasePage serves as the abstract foundation with common WebDriver operations
* Each page class encapsulates specific page elements and behaviors
* Clean separation between test logic and page interactions
* Reusable methods for element interactions (click, getText, sendKeys)


### 2. Factory Pattern
**Classes**: BrowserFactory, DriverManager

**Implementation:**
* BrowserFactory creates browser instances based on configuration
* DriverManager manages WebDriver lifecycle using factory-created instances
* Supports multiple browser types (Chrome, Firefox) with consistent interface
* Handles browser-specific configurations and options


### 3. Singleton Pattern
**Classes**: ConfigManager, DriverManager

**Implementation:**
* ConfigManager uses static methods with thread-safe initialization
* DriverManager maintains ThreadLocal WebDriver instances
* Single point of configuration access throughout the framework
* Thread-safe implementation for parallel execution


### 4. Builder Pattern (Implicit)
**Classes**: CheckoutPage, MenuPage

**Implementation:**
* Methods like fillPaymentForm() chain multiple operations
* Fluent interface for complex page interactions
* Step-by-step object construction for test scenarios


### 5. Template Method Pattern
**Classes**: BaseTest, Test Classes

**Implementation:**
* BaseTest defines test execution template (setup → test → teardown)
* Subclasses override specific methods while maintaining overall structure
* Consistent test lifecycle management across all test classes


### 6. Observer Pattern
**Classes**: TestListener

**Implementation:**
* TestListener implements ITestListener to observe test events
* Automatic screenshot capture on failures
* Comprehensive test execution logging and reporting


### 7. Strategy Pattern
**Classes**: ConfigManager, Browser-specific handling

**Implementation:**
* Different strategies for Chrome vs Firefox browser handling
* Configurable timeout strategies based on browser type
* Adaptive behavior for browser-specific limitations


### 8. Data Provider Pattern
**Classes**: TestDataProvider, ExcelReader, ExcelUtil

**Implementation:**
* Centralized test data management
* Multiple data sources (hardcoded, CSV, Excel)
* Parameterized test execution with various data sets

## Git Push & Compress file details:

**cd /path/to/your/project && git init && git add . && git commit -m "Initial commit - add Coffee Cart Automation project" && git branch -M main && git remote add origin https://github.com/JeevanBabuVC/Automation-Projects.git && git push -u origin main**

* cd → navigates to your project folder.
* git init → initializes the repo.
* git add . → stages all files.
* git commit -m "[Message]" → commits with a message.
* git branch -M main → ensures the branch is main.
* git remote add origin ... → adds your GitHub repo as remote.
* git push -u origin main → pushes all changes to GitHub.

#### To zip the project folder:
**tar -czf CoffeeCart-Automation.tar.gz CoffeeCart-Automation/**

## Setup Options

### Option 1: Using Zip File

**Extract the project**

1. unzip CoffeeCart-Automation.zip
2. cd CoffeeCart-Automation
3. ls -la

### Option 2: Using Git Repository

**Clone the repository**

1. git clone <repository-url>
2. cd CoffeeCart-Automation
3. git status
4. ls -la

### Install Dependency: 

**mvn clean install** run this command in terminal 

### Run Specific Tests:

#### Menu tests only
**mvn test -Dtest=MenuTests**

#### Cart tests only
**mvn test -Dtest=CartTests**

#### Checkout tests only
**mvn test -Dtest=CheckoutTests**

#### End-to-end tests only
**mvn test -Dtest=EndToEndTests**

### Browser specific execution:

#### Run with Chrome
**mvn test -Dbrowser=chrome**

#### Run with Firefox
**mvn test -Dbrowser=firefox**

#### Run in headless mode
**mvn test -Dheadless=true**

#### Using TestNG xml:
mvn test -DsuiteXmlFile=src/test/resources/testng-optimized.xml

#### Programmatic execution:
Run the CoffeeCartApp file.

# Complete test run
mvn clean test

### Reports:

# TestNG reports
cd target/surefire-reports/index.html

# Allure reports (if configured)
allure serve target/allure-results

## Conclusion:

The framework successfully combines:
* 8 major design patterns
* Comprehensive test cases with 96% pass rate (intentional failures excluded)
* Cross-browser compatibility with browser-specific optimizations
* Features including automatic failure handling, detailed reporting, and parallel execution support