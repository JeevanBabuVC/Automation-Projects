package com.coffeecart.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.coffeecart.managers.ConfigManager;
import com.coffeecart.utils.LoggerUtil;

/**
 * Manages shopping cart operations including item quantity changes, removal, and total calculations.
 */
public class CartPage extends BasePage{

    @FindBy(xpath = "//div[contains(text(),'$')]")
    private List<WebElement> totalPrices;

    @FindBy(xpath="//div[normalize-space()='(Discounted) Mocha']")
    private WebElement discountedMocha;

    @FindBy(xpath = "//div[contains(@class,'cup-body')]")
    private List<WebElement> itemNames;

    @FindBy(css = "a[href='/cart']")
    private WebElement cartLink;

    @FindBy(css = ".cart-preview .list-item")
    private List<WebElement> cartItems;

    @FindBy(xpath = "//p[normalize-space()='No coffee, go add some.']")
    private WebElement emptyCartinfo;

    @FindBy(xpath = "//div[@class='pay-container']")
    private WebElement totalButton;

    @FindBy(css = ".unit-controller button[aria-label*='Add']")
    private List<WebElement> increaseQuantityButton;

    @FindBy(css = ".unit-controller button[aria-label*='Remove']")
    private List<WebElement> decreaseQuantityButton;

    // Initializes the CartPage
    public CartPage(){
        super();
    }

    // Gets the current text displayed on the cart link including the item count
    public String getCartText() {
        try {
            wait.until(ExpectedConditions.visibilityOf(cartLink));
            String text = cartLink.getText();
            LoggerUtil.info("Successfully fetched cart text: " + text);
            return text;
        } catch (Exception e) {
            LoggerUtil.error("Failed to get cart text", e);
            throw e;
        }
    }

    // Navigates to the shopping cart page by clicking the cart link
    public void navigatetoCart() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cartLink));
            cartLink.click();
            LoggerUtil.info("Successfully navigated to cart page");
        } catch (Exception e) {
            LoggerUtil.error("Failed to navigate to cart", e);
            throw e;
        }
    }

    /**
     * Waits for the cart to update after items are added or modified.
     * Ensures the cart text changes from empty state before proceeding with tests.
     */
    public void waitForCartUpdate(int expectedItems) {
        try {
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBePresentInElement(cartLink, ConfigManager.getCartEmptyText())
            ));
            LoggerUtil.info("Cart updated successfully");
        } catch (Exception e) {
            LoggerUtil.error("Failed to wait for cart update", e);
            throw e;
        }
    }

    // Checks if the empty cart message is displayed when no items are present
    public boolean isEmptyCartMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(emptyCartinfo));
            boolean isDisplayed = emptyCartinfo.isDisplayed();
            LoggerUtil.info("Empty cart message displayed: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            LoggerUtil.error("Failed to check empty cart message", e);
            return false;
        }
    }

    // Verifies if the discounted Mocha item is present in the cart
    public boolean isDiscountedMochaPresentInCart() {
        try {
            wait.until(ExpectedConditions.visibilityOf(discountedMocha));
            boolean isPresent = discountedMocha.getText().contains("(Discounted) Mocha");
            LoggerUtil.info("Discounted Mocha present: " + isPresent);
            return isPresent;
        } catch (Exception e) {
            LoggerUtil.error("Failed to check for discounted Mocha", e);
            return false;
        }
    }

    // Checks if quantity increase and decrease buttons are present for cart items
    public boolean areQuantityControlsPresent() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".unit-controller button")));
            boolean increasePresent = !increaseQuantityButton.isEmpty();
            boolean decreasePresent = !decreaseQuantityButton.isEmpty();
            LoggerUtil.info("Quantity controls presence - Increase: " + increasePresent + ", Decrease: " + decreasePresent);
            return increasePresent && decreasePresent;
        } catch (Exception e) {
            LoggerUtil.error("Failed to verify quantity controls presence", e);
            return false;
        }
    }

    /**
     * Waits for the cart page to fully load by checking the URL contains '/cart'.
     * Ensures page navigation is complete before running cart-specific tests.
     */
    public void waitForPageLoad() {
        try {
            wait.until(ExpectedConditions.urlContains("/cart"));
            LoggerUtil.info("Cart page loaded successfully");
        } catch (Exception e) {
            LoggerUtil.error("Failed to wait for page load", e);
            throw e;
        }
    }
}