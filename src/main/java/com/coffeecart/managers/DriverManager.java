package com.coffeecart.managers;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages WebDriver instances with thread-safe operations.
 */
public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static final BrowserFactory browserFactory = new BrowserFactory();

    // Gets the current WebDriver instance using the default browser from config
    public static WebDriver getDriver() {
        return getDriver(ConfigManager.getBrowser());
    }

    // Creates or returns existing WebDriver for the specified browser type
    public static WebDriver getDriver(String browser) {
        if (driverThread.get() == null) {
            try {
                WebDriver driver = browserFactory.createDriver(browser);
                driverThread.set(driver);
                logger.info("Successfully created new WebDriver instance for browser: {}", browser);
            } catch (Exception e) {
                logger.error("Failed to create the WebDriver: {}", e.getMessage());
                throw e;
            }
        }
        return driverThread.get();
    }


    // Safely closes the WebDriver
    public static void closeDriver() {
        try {
            WebDriver driver = driverThread.get();
            if (driver != null) {
                driver.quit();
                driverThread.remove();
                browserFactory.quitDriver();
                logger.info("Successfully closed the driver");
            }
        } catch (Exception e) {
            logger.error("Failed to close WebDriver and got error: {}", e.getMessage());
            throw e;
        }
    }

    // Resets the driver by closing current instance and clearing thread storage
    public static void resetDriver() {
        closeDriver();
        driverThread.remove();
    }
}
