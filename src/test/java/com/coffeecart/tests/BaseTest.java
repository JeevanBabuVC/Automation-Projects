package com.coffeecart.tests;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.coffeecart.managers.ConfigManager;
import com.coffeecart.managers.DriverManager;
import com.coffeecart.utils.LoggerUtil;

//Base class for all test classes providing common setup, teardown, and utility methods.

public class BaseTest {
    protected static WebDriver driver;
    protected String browserType;
    private static boolean isInitialized = false;

    // Sets up the browser and navigates to the application
    @Parameters("browser")
    @BeforeClass(alwaysRun = true)
    public void setupClass(@Optional("chrome") String browserParam) {
        try {
            browserType = browserParam.trim().toLowerCase();
            if (!isInitialized) {
                if (driver != null) {
                    DriverManager.closeDriver();
                }
                driver = DriverManager.getDriver(browserType);
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigManager.getImplicitWait()));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigManager.getPageLoadTimeout()));

                navigateWithRetry();
                isInitialized = true;
            }

            LoggerUtil.info("Test setup has been initialized with browser: " + browserType);
        } catch (Exception e) {
            LoggerUtil.error("Failed to setup test class: " + e.getMessage());
            if (driver != null) {
                DriverManager.closeDriver();
                driver = null;
            }
            throw e;
        }
    }

    private void navigateWithRetry() {
        int maxRetries = ConfigManager.getNavigationRetryCount();
        int retryDelay = ConfigManager.getNavigationRetryDelay();

        for (int i = 0; i < maxRetries; i++) {
            try {
                LoggerUtil.info("Attempting to navigate to " + ConfigManager.getBaseUrl() + " (attempt " + (i + 1) + "/"
                        + maxRetries + ")");
                driver.get(ConfigManager.getBaseUrl());
                LoggerUtil.info("Successfully navigated to the coffee cart application");
                return;
            } catch (Exception e) {
                LoggerUtil.error("Navigation attempt " + (i + 1) + " has been failed: " + e.getMessage());
                if (i == maxRetries - 1) {
                    throw new RuntimeException("Failed to navigate after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Runs before each test method
    @BeforeMethod(alwaysRun = true)
    public void setupMethod() {
        // Override in subclasses if specific setup is needed
    }

    // Runs after each test method for cleanup like closing popups or resetting state
    @AfterMethod(alwaysRun = true)
    public void tearDownMethod() {
        // Override in subclasses if specific cleanup is needed
    }

    // Logs completion of test class execution for the current browser
    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        LoggerUtil.info("Test class completed for browser: " + browserType);
    }

    // Closes all browser instances and cleans up resources
    @AfterSuite(alwaysRun = true)
    public static void tearDownSuite() {
        try {
            if (driver != null) {
                DriverManager.closeDriver();
                driver = null;
            }
            isInitialized = false;
            LoggerUtil.info("Browser session closed after all tests");
        } catch (Exception e) {
            LoggerUtil.error("Failed to close browser session: " + e.getMessage());
        }
    }

    protected void navigateToPage() {
        if (driver != null) {
            navigateWithRetry();
        }
    }

    // Returns the current WebDriver instance
    public WebDriver getDriver() {
        return driver;
    }
}
