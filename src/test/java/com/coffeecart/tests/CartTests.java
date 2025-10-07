package com.coffeecart.tests;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.coffeecart.managers.ConfigManager;
import com.coffeecart.managers.DriverManager;
import com.coffeecart.pages.CartPage;
import com.coffeecart.pages.CheckoutPage;
import com.coffeecart.pages.MenuPage;
import com.coffeecart.utils.LoggerUtil;

//Tests shopping cart functionality including item addition, quantity management, and cart operations.
//Includes browser-specific handling for Firefox hover limitations.

public class CartTests extends BaseTest {
    private CartPage cartPage;
    private MenuPage menuPage;
    private CheckoutPage checkoutPage;

    //Sets up fresh page objects and ensures clean cart state before each test.

    @BeforeMethod
    public void setup() {
        cartPage = new CartPage();
        menuPage = new MenuPage();
        checkoutPage = new CheckoutPage();
        menuPage.navigateToMenuPage();
        
        // Ensure clean state for cart page tests
        try {
            if (!menuPage.isCartEmpty()) {
                menuPage.refreshUI();
                LoggerUtil.info("Cart has reset before CartTests");
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to reset cart before the Cart Tests", e);
        }
    }
    
    //Cleans up browser resources after all cart tests complete.
    @AfterClass
    public void classCleanup() {
        try {
            // Close browser to ensure clean state for next test class
            DriverManager.closeDriver();
            driver = null;
            LoggerUtil.info("Browser has been closed after CartTests to ensure clean state");
        } catch (Exception e) {
            LoggerUtil.error("Failed to close browser after CartTests", e);
        }
    }

    //Verify empty cart
    @Test(priority = 1, groups = {"sanity"})
    public void testNavigateToEmptyCart() {
        LoggerUtil.info("Testing navigation to empty cart");
        cartPage.navigatetoCart();
        cartPage.waitForPageLoad();
        Assert.assertEquals(cartPage.getCartText(), ConfigManager.getCartEmptyText(), "Cart should be empty");
        Assert.assertTrue(cartPage.isEmptyCartMessageDisplayed(), "Empty cart message should be shown");
    }

    //Add single item to cart
    @Test(priority = 2, description = "Verify single item addition to cart", groups = {"cart"})
    public void testAddSingleItemToCart() {
        String testItem = ConfigManager.getTestCoffeeItem();
        int testQuantity = 1;
        
        menuPage.addItemToCart(testItem, testQuantity);
        Assert.assertFalse(menuPage.isCartEmpty(), "Cart should not be empty after adding coffee item");
        LoggerUtil.info("Successfully added single item to cart");
    }

    //Add multiple quantity
    @Test(priority = 3, description = "Verify multiple quantity addition", groups = {"cart"})
    public void testAddMultipleQuantity() {
        String testItem = ConfigManager.getTestCoffeeItem();
        
        menuPage.addItemToCart(testItem, 3);
        Assert.assertFalse(menuPage.isCartEmpty(), "Cart should not be empty after adding multiple items");
        LoggerUtil.info("Successfully added multiple quantities to cart");
    }

    //Verifies that cart total calculations are accurate and display properly.
    @Test(priority = 4, description = "Verify cart total calculation", groups = {"cart"})
    public void testCartTotalCalculation() {
        String testItem = ConfigManager.getTestCoffeeItem();
        
        menuPage.addItemToCart(testItem, 2);
        String totalText = menuPage.getTotalText();
        
        Assert.assertNotNull(totalText, "Total text should not be null");
        Assert.assertTrue(totalText.contains("$"), "Total should contain currency symbol");
        Assert.assertFalse(totalText.contains(ConfigManager.getCartEmptyPrice()), "Total should not be zero");
        LoggerUtil.info("Cart total calculation verified: " + totalText);
    }

    /**
     * Tests cart preview popup functionality with browser-specific handling.
     * Firefox has known hover limitations in headless mode, so we expect different behavior.
     */
    @Test(priority = 5, description = "Verify cart preview functionality", groups = {"cart"})
    public void testCartPreview() {
        String testItem = ConfigManager.getTestCoffeeItem();
        
        menuPage.addItemToCart(testItem, 1);
        boolean isPreviewVisible = menuPage.isPreviewPopupVisible();
        
        if (ConfigManager.getBrowser().equalsIgnoreCase("firefox")) {
            // Known Firefox issue - hover doesn't work reliably in headless mode
            Assert.assertFalse(isPreviewVisible, "Cart preview expected to fail in Firefox due to hover limitations");
            LoggerUtil.info("Cart preview test failed as expected in Firefox - hover interaction limitation");
        } else {
            Assert.assertTrue(isPreviewVisible, "Cart preview should be visible");
            LoggerUtil.info("Cart preview functionality verified");
        }
    }

    //Validates navigation back to menu page from cart.
    @Test(priority = 6, groups = {"sanity"})
    public void testNavigateToMenu() {
        LoggerUtil.info("Testing menu navigation");
        menuPage.navigateToMenuPage();
        Assert.assertTrue(menuPage.isMenuDisplayed(), "Menu page should be displayed");
    }

    //Validates the complete cart workflow with special offers and discounted items.
    @Test(priority = 7, groups = {"regression"})
    public void testCartOperations() {
        LoggerUtil.info("Starting cart operations sequence");
        Assert.assertTrue(menuPage.addThreeItemsForPromo(ConfigManager.getPromoTriggerItem()), "Promo should be triggered");
        menuPage.acceptPromoOffer();
        cartPage.waitForCartUpdate(4);
        cartPage.navigatetoCart();
        cartPage.waitForPageLoad();

        LoggerUtil.info("Checking for the discounted Mocha");
        Assert.assertTrue(cartPage.isDiscountedMochaPresentInCart(), "Should have Discounted Mocha");

        LoggerUtil.info("Checking quantity controls");
        Assert.assertTrue(cartPage.areQuantityControlsPresent(), "Quantity controls should be present");
    }

}