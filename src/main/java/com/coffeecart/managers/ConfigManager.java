package com.coffeecart.managers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.coffeecart.utils.LoggerUtil;

/**
 * Manages all configuration settings from properties file with caching for
 * better performance.
 * Provides easy access to timeouts, URLs, test data, and browser settings
 * throughout the framework.
 */
public class ConfigManager {
    private static final Properties properties = new Properties();
    private static final ConcurrentHashMap<String, String> propertyCache = new ConcurrentHashMap<>();
    private static final String CONFIG_FILE = "resources/config.properties";
    private static volatile boolean isInitialized = false;

    static {
        loadProperties();
    }

    // Loads configuration from properties file with thread-safe initialization
    private static void loadProperties() {
        if (!isInitialized) {
            synchronized (ConfigManager.class) {
                if (!isInitialized) {
                    try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
                        properties.load(input);
                        isInitialized = true;
                        LoggerUtil.info("Configuration properties loaded successfully");
                    } catch (IOException e) {
                        LoggerUtil.error("Failed to load configuration properties", e);
                        throw new RuntimeException("Failed to load config file: " + CONFIG_FILE, e);
                    }
                }
            }
        }
    }

    // Gets the property value
    private static String getPropertyValue(String key) {
        return propertyCache.computeIfAbsent(key, k -> {
            String value = System.getProperty(k);
            if (value == null || value.trim().isEmpty()) {
                value = properties.getProperty(k);
            }
            if (value == null || value.trim().isEmpty()) {
                LoggerUtil.error("Missing or empty property: " + k);
                throw new RuntimeException("Property '" + k + "' not found or empty in config file");
            }
            return value.trim();
        });
    }

    // Returns the browser to use, checking system properties first then config file
    public static String getBrowser() {
        String systemBrowser = System.getProperty("browser");
        if (systemBrowser != null && !systemBrowser.trim().isEmpty()) {
            return systemBrowser.trim();
        }
        String configBrowser = properties.getProperty("browser");
        if (configBrowser == null || configBrowser.trim().isEmpty()) {
            throw new RuntimeException("Property 'browser' not found or empty in config file");
        }
        return configBrowser.trim();
    }

    // Gets the application URL
    public static String getBaseUrl() {
        return getPropertyValue("url");
    }

    // Returns implicit wait timeout in seconds
    public static int getImplicitWait() {
        return Integer.parseInt(getPropertyValue("implicit.wait"));
    }

    // Returns explicit wait timeout for specific conditions
    public static int getExplicitWait() {
        return Integer.parseInt(getPropertyValue("explicit.wait"));
    }

    // Gets page load timeout to prevent hanging on slow pages
    public static int getPageLoadTimeout() {
        return Integer.parseInt(getPropertyValue("page.load.timeout"));
    }

    // Checks if tests should run in headless mode (no browser UI)
    public static boolean isHeadless() {
        return Boolean.parseBoolean(getPropertyValue("headless"));
    }

    // Returns the screenshot directory path
    public static String getScreenshotPath() {
        return getPropertyValue("screenshot.path");
    }

    // Gets the number of times to retry navigation if page fails to load
    public static int getNavigationRetryCount() {
        return Integer.parseInt(getPropertyValue("navigation.retry.count"));
    }

    // Returns delay between navigation retry attempts
    public static int getNavigationRetryDelay() {
        return Integer.parseInt(getPropertyValue("navigation.retry.delay"));
    }

    // Returns Chrome browser options as an array for custom browser configuration
    public static String[] getChromeOptions() {
        String options = properties.getProperty("chrome.options");
        if (options == null || options.trim().isEmpty()) {
            return new String[0];
        }
        return options.split(",");
    }

    // Returns the attribute name for element identification
    public static String getItemAriaLabel() {
        return "aria-label";
    }

    // Returns the text attribute name for getting element text content
    public static String getItemTextAttribute() {
        return "text";
    }

    // Gets a property value with a fallback default if the key doesn't exist
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // Returns wait time for element interactions
    public static int getElementWaitTime() {
        return Integer.parseInt(getPropertyValue("element.wait.time"));
    }

    // Gets wait time for promotional offers to appear
    public static int getPromoWaitTime() {
        return Integer.parseInt(getPropertyValue("promo.wait.time"));
    }

    // Returns wait time for cart updates to complete
    public static int getCartUpdateWait() {
        return Integer.parseInt(getPropertyValue("cart.update.wait"));
    }

    // Gets the expected text when cart is empty
    public static String getCartEmptyText() {
        return getPropertyValue("cart.empty.text");
    }

    // Returns the expected price display when cart is empty
    public static String getCartEmptyPrice() {
        return getPropertyValue("cart.empty.price");
    }

    // Gets wait time between quantity increment clicks
    public static int getQuantityIncrementWait() {
        return Integer.parseInt(getPropertyValue("quantity.increment.wait"));
    }

    // Returns CSS selector for the total button element
    public static String getTotalButtonSelector() {
        return getPropertyValue("total.button.selector");
    }

    // Returns CSS selector for the checkout total amount element
    public static String getCheckoutTotalSelector() {
        return getPropertyValue("checkout.total.selector");
    }

    // Returns wait time for snackbar notifications to appear
    public static int getSnackbarWaitTime() {
        return Integer.parseInt(getPropertyValue("snackbar.wait.time"));
    }

    // Gets wait time in seconds for checkout form to load properly
    public static int getCheckoutFormWait() {
        return Integer.parseInt(getPropertyValue("checkout.form.wait"));
    }

    // Gets wait time in milliseconds for DOM to stabilize after dynamic changes
    public static int getDomStabilityWait() {
        return Integer.parseInt(getPropertyValue("dom.stability.wait"));
    }

    // Returns wait time in milliseconds for overlay popups to be dismissed
    public static int getOverlayDismissWait() {
        return Integer.parseInt(getPropertyValue("overlay.dismiss.wait"));
    }

    // Returns wait time in milliseconds between element interactions
    public static int getElementInteractionWait() {
        return Integer.parseInt(getPropertyValue("element.interaction.wait"));
    }

    // Gets wait time in milliseconds for page to stabilize after refresh
    public static int getPageRefreshStabilityWait() {
        return Integer.parseInt(getPropertyValue("page.refresh.stability.wait"));
    }

    // Returns the default coffee item name used in tests
    public static String getTestCoffeeItem() {
        return getPropertyValue("test.coffee.item");
    }

    // Gets the default test user name for form filling
    public static String getDefaultTestName() {
        return getPropertyValue("default.test.name");
    }

    // Returns the default test email address for form validation
    public static String getDefaultTestEmail() {
        return getPropertyValue("default.test.email");
    }

    // Gets an invalid email format for negative testing scenarios
    public static String getInvalidTestEmail() {
        return getPropertyValue("invalid.test.email");
    }

    // Returns the expected price for discounted Mocha in promotional offers
    public static String getDiscountedMochaPrice() {
        return getPropertyValue("discounted.mocha.price");
    }

    // Gets the maximum number of items allowed in cart
    public static int getMaxCartItems() {
        return Integer.parseInt(getPropertyValue("max.cart.items"));
    }

    // Returns the coffee item that triggers promotional offers
    public static String getPromoTriggerItem() {
        return getPropertyValue("promo.trigger.item");
    }

    // Gets the minimum price for valid price range
    public static double getPriceRangeMin() {
        return Double.parseDouble(getPropertyValue("price.range.min"));
    }

    // Returns the maximum price for valid price range
    public static double getPriceRangeMax() {
        return Double.parseDouble(getPropertyValue("price.range.max"));
    }

    // Gets the minimum price for invalid range testing (should fail)
    public static double getInvalidPriceRangeMin() {
        return Double.parseDouble(getPropertyValue("invalid.price.range.min"));
    }

    // Returns the maximum price for invalid range testing (should fail)
    public static double getInvalidPriceRangeMax() {
        return Double.parseDouble(getPropertyValue("invalid.price.range.max"));
    }

    // Gets the price threshold for invalid price testing (should fail)
    public static double getInvalidPriceThreshold() {
        return Double.parseDouble(getPropertyValue("invalid.price.threshold"));
    }

    // Returns wait time for cart preview to appear on hover
    public static int getCartPreviewWait() {
        return Integer.parseInt(getPropertyValue("cart.preview.wait"));
    }

    // Gets Firefox-specific wait time for cart preview (this is due to browser
    // interaction differences)
    public static int getFirefoxCartPreviewWait() {
        return Integer.parseInt(getPropertyValue("firefox.cart.preview.wait"));
    }

    // Reloads all configuration properties and clears cache [Useful for future]
    public static void refreshConfiguration() {
        synchronized (ConfigManager.class) {
            propertyCache.clear();
            isInitialized = false;
            loadProperties();
        }
    }
}
