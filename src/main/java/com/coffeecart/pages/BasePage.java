package com.coffeecart.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.coffeecart.managers.ConfigManager;
import com.coffeecart.managers.DriverManager;
import com.coffeecart.utils.LoggerUtil;

/**
 * Base class for all page objects providing common WebDriver operations and wait strategies.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Initializes the page with WebDriver and sets up element locators automatically
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigManager.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    // Simple wait for element visibility
    protected void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }


    // Waits for an element to become visible
    protected void waitForElementVisibility(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            LoggerUtil.error("Element not visible: " + e.getMessage());
            throw e;
        }
    }

    // Ensures an element is clickable before attempting to interact with it
    protected void checkElementClickableState(WebElement element) {
        wait.until((ExpectedConditions.elementToBeClickable(element)));
    }

    // Waits for element to be both visible and clickable for safe interaction
    public void waitForElementClickable(WebElement element) {
        wait.until(ExpectedConditions.and(
                ExpectedConditions.visibilityOf(element),
                ExpectedConditions.elementToBeClickable(element)
        ));
    }

    // Clicks an element after ensuring that it is clickable with error handling
    protected void clickElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            LoggerUtil.error("Failed to click element", e);
            throw e;
        }
    }

    // Gets text from an element after waiting for it to be visible
    protected String getText(WebElement element) {
        waitForElementVisibility(element);
        return element.getText();
    }

    // Types text into an input field
    protected void sendKeys(WebElement element, String text) {
        waitForElementVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    // Checks if an element is displayed without throwing exceptions for missing elements
    protected boolean isWebElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}