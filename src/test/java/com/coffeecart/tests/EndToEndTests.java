package com.coffeecart.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.coffeecart.managers.ConfigManager;
import com.coffeecart.pages.CheckoutPage;
import com.coffeecart.pages.MenuPage;
import com.coffeecart.utils.ExcelUtil;
import com.coffeecart.utils.LoggerUtil;

//Tests complete user journeys from initial menu interaction through final order completion.
public class EndToEndTests extends BaseTest {
    private MenuPage menuPage;
    private CheckoutPage checkoutPage;
    

    @BeforeClass
    public void classSetup() {
        menuPage = new MenuPage();
        checkoutPage = new CheckoutPage();
    }

    @Test(description = "Complete end-to-end coffee ordering workflow", groups = {"regression"})
    public void testCompleteOrderWorkflow() {
        LoggerUtil.info("Starting complete order to check workflow test");

        // 1. Verify menu page is loaded
        Assert.assertTrue(menuPage.isMenuDisplayed(), "Menu page should be loaded");
        LoggerUtil.info("Menu page has been loaded successfully");

        //2. Navigate to fresh menu page
        menuPage.navigateToMenuPage();
        LoggerUtil.info("Successfully navigated to fresh menu page");

        // 3. Add item to cart
        String testItem = ConfigManager.getTestCoffeeItem();
        int testQuantity = 1;
        menuPage.addItemToCart(testItem, testQuantity);
        LoggerUtil.info("Successfully added " + testQuantity + " " + testItem + " item(s) to the cart");

        // 4. Verify cart total shows items
        String initialTotal = menuPage.getTotalText();
        Assert.assertFalse(initialTotal.contains(ConfigManager.getCartEmptyPrice()), "Cart should not be empty after adding items");
        LoggerUtil.info("Verify cart should contains items");

        // 5.Get cart total from menu page
        String cartTotal = menuPage.getTotalText();
        Assert.assertNotNull(cartTotal, "Cart total should not be null");
        Assert.assertTrue(cartTotal.contains("$"), "Cart total should contain currency symbol");
        LoggerUtil.info("Cart total: " + cartTotal);

        //6. Proceed to the checkout
        menuPage.clickTotalButton();
        Assert.assertTrue(checkoutPage.ischeckoutFormdisplayed(), "Checkout form should be displayed");
        LoggerUtil.info("Proceeded to the checkout");

        //7. Fill customer details
        Map<String, String> testData = ExcelUtil.getTestData("resources/testdata.csv", "TestData", "testCompleteOrderWorkflow");
        String customerName = testData.getOrDefault("Name", ConfigManager.getDefaultTestName());
        String customerEmail = testData.getOrDefault("Email", ConfigManager.getDefaultTestEmail());
        
        // Wait for the form to be ready
        try {
            Thread.sleep(ConfigManager.getCheckoutFormWait() * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        checkoutPage.enterCustomername(customerName);
        checkoutPage.enterEmailid(customerEmail);
        LoggerUtil.info("Successfully ntered customer details: " + customerName + ", " + customerEmail);

        // 8. Verify form validation
        Assert.assertTrue(checkoutPage.validateForm(customerName, customerEmail), "Form should be valid with proper details");
        LoggerUtil.info("Test for form validation has  passed");

        // 9. Verify the total amount in checkout
        String checkoutTotal = checkoutPage.getTotalAmount();
        Assert.assertNotNull(checkoutTotal, "Checkout total should not be null");
        Assert.assertTrue(checkoutTotal.contains("$"), "Checkout total should contain currency symbol");
        LoggerUtil.info("Checkout total: " + checkoutTotal);

        // 10. If everything is fine then Place order
        checkoutPage.placeOrder();
        LoggerUtil.info("Order has been submitted successfully");

        LoggerUtil.info(" successfully completed end-to-end workflow test");
    }
}