package com.coffeecart.managers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Creates and configures browser instances for test execution with proper options.
 * Handles both Chrome and Firefox browsers with headless mode and custom configurations.
 */
public class BrowserFactory {
    private static final Logger logger = LoggerFactory.getLogger(BrowserFactory.class);
    private static final ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

    // Sets up a new browser instance
    public WebDriver createDriver(String browser) {
        WebDriver driver;
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (ConfigManager.isHeadless()) {
                        chromeOptions.addArguments("--headless=new");
                    }
                    String[] configuredOptions = ConfigManager.getChromeOptions();
                    for (String option : configuredOptions) {
                        chromeOptions.addArguments(option.trim());
                    }
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (ConfigManager.isHeadless()) {
                        firefoxOptions.addArguments("--headless");
                    }
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                default:
                    throw new IllegalArgumentException("Browser " + browser + " not supported");
            }
            drivers.set(driver);
            return driver;
        } catch (Exception e) {
            logger.error("Failed to create driver for browser {}: {}", browser, e.getMessage());
            throw new RuntimeException("Failed to create driver", e);
        }
    }

    // Safely closes the browser
    public void quitDriver() {
        try {
            WebDriver driver = drivers.get();
            if (driver != null) {
                driver.quit();
                drivers.remove();
                logger.info("Driver quit successfully");
            }
        } catch (Exception e) {
            logger.error("Error quitting driver: {}", e.getMessage());
            throw new RuntimeException("Failed to quit driver", e);
        }
    }
}
