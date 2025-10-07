package com.coffeecart.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.coffeecart.data.TestDataProvider;
import com.coffeecart.managers.ConfigManager;
import com.coffeecart.pages.CheckoutPage;
import com.coffeecart.pages.MenuPage;
import com.coffeecart.utils.ExcelUtil;
import com.coffeecart.utils.ScreenshotUtil;

//Tests checkout form functionality including customer details entry, validation, and order submission.

public class CheckoutTests extends BaseTest {
    private CheckoutPage checkoutPage;
    private MenuPage menuPage;


    @BeforeClass
    public void classSetup() {
        checkoutPage = new CheckoutPage();
        menuPage = new MenuPage();
    }
    

    @BeforeMethod
    public void setup() {
        try {
            if (checkoutPage.ischeckoutFormdisplayed()) {
                checkoutPage.closeCheckout();
            }
        } catch (Exception e) {
        }
        menuPage.navigateToMenuPage();
        String testItem = ConfigManager.getTestCoffeeItem();
        int testQuantity = 1;
        
        menuPage.addItemToCart(testItem, testQuantity);
        menuPage.clickTotalButton();
    }


    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (checkoutPage.ischeckoutFormdisplayed()) {
                checkoutPage.closeCheckout();
            }
        } catch (Exception e) {
        }
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtil.captureScreenshot(driver, result.getName());
        }
    }

    @Test(priority = 1)
    public void testCheckoutFormDisplay() {
        boolean isDisplayed = checkoutPage.ischeckoutFormdisplayed();
        Assert.assertTrue(isDisplayed, "Checkout form should be displayed after clicking the total button");
    }

    @Test(priority = 2)
    public void testInitialFormState() {
        boolean isInitialState = checkoutPage.isFormInInitialState();
        boolean areFieldsEmpty = checkoutPage.isEmptyField();
        Assert.assertTrue(isInitialState, "Form should be in the initial state when opened");
        Assert.assertTrue(areFieldsEmpty, "All form fields should be empty initially");
        Assert.assertFalse(checkoutPage.isPromoOfferSelected(), "Promo offer should not be selected by default");
    }

    @Test(priority = 3)
    public void testEnterCustomerDetails() {
        Map<String, String> testData = ExcelUtil.getTestData("resources/testdata.csv", "TestData", "testEnterCustomerDetails");
        String testName = testData.getOrDefault("Name", ConfigManager.getDefaultTestName());
        String testEmail = testData.getOrDefault("Email", ConfigManager.getDefaultTestEmail());

        checkoutPage.enterCustomername(testName);
        checkoutPage.enterEmailid(testEmail);

        String actualName = checkoutPage.getCurrentname();
        String actualEmail = checkoutPage.getCurrentemailid();

        Assert.assertEquals(actualName, testName, "Entered name should match the expected name");
        Assert.assertEquals(actualEmail, testEmail, "Entered email should match the expected email");
        Assert.assertFalse(checkoutPage.isEmptyField(), "Fields should not be empty after entering details");
    }

    @Test(priority = 4)
    public void testPromoOfferToggle() {
        boolean initialState = checkoutPage.isPromoOfferSelected();
        Assert.assertFalse(initialState, "Promo offer should be unchecked initially");

        checkoutPage.togglePromoOffer();
        boolean toggledState = checkoutPage.isPromoOfferSelected();
        Assert.assertTrue(toggledState, "Promo offer should be checked after toggling");

        checkoutPage.togglePromoOffer();
        boolean finalState = checkoutPage.isPromoOfferSelected();
        Assert.assertFalse(finalState, "Promo offer should be unchecked");
    }

    @Test(priority = 5)
    public void testEmailValidation() {
        String invalidEmail = "invalid-email-format";
        checkoutPage.enterEmailid(invalidEmail);
        Assert.assertFalse(checkoutPage.isValidEmailAddress(invalidEmail), "Invalid email format should not pass validation");

        String validEmail = ConfigManager.getDefaultTestEmail();
        checkoutPage.enterEmailid(validEmail);
        Assert.assertTrue(checkoutPage.isValidEmailAddress(validEmail), "Valid email format should pass validation");
    }

    @Test(priority = 6)
    public void testFillPaymentForm() {
        Map<String, String> testData = ExcelUtil.getTestData("resources/testdata.csv", "TestData", "testFillPaymentForm");
        String testName = testData.getOrDefault("Name", ConfigManager.getDefaultTestName());
        String testEmail = testData.getOrDefault("Email", ConfigManager.getDefaultTestEmail());

        checkoutPage.fillPaymentForm(testName, testEmail, true);

        Assert.assertTrue(checkoutPage.validateForm(testName, testEmail), "Form should be valid with correct details");
        Assert.assertTrue(checkoutPage.isPromoOfferSelected(), "Promo offer should be selected as specified");
        Assert.assertEquals(checkoutPage.getCurrentname(), testName, "Name field should contain entered name");
        Assert.assertEquals(checkoutPage.getCurrentemailid(), testEmail, "Email field should contain entered email");
    }

    @Test(priority = 7)
    public void testSubmitButtonState() {
        checkoutPage.fillPaymentForm(ConfigManager.getDefaultTestName(), ConfigManager.getDefaultTestEmail(), false);
        boolean afterFilling = checkoutPage.isSubmitButtonEnabled();
        Assert.assertTrue(afterFilling, "Submit button should be enabled after filling valid details");
    }

    @Test(priority = 8)
    public void testGetTotalAmount() {
        String totalAmount = checkoutPage.getTotalAmount();
        Assert.assertFalse(totalAmount.isEmpty(), "Total amount should not be empty");

        String amountValue = totalAmount.replace("Total: ", "");
        Assert.assertTrue(amountValue.matches("\\$\\d+\\.\\d{2}"),
                "Total amount " + amountValue + " should be in valid currency format ($XX.XX)");
    }

    @Test(priority = 9)
    public void testPlaceOrder() {
        Map<String, String> testData = ExcelUtil.getTestData("resources/testdata.csv", "TestData", "testPlaceOrder");
        String testName = testData.getOrDefault("Name", ConfigManager.getDefaultTestName());
        String testEmail = testData.getOrDefault("Email", ConfigManager.getDefaultTestEmail());
        
        checkoutPage.fillPaymentForm(testName, testEmail, false);
        Assert.assertTrue(checkoutPage.isSubmitButtonEnabled(), "Submit button should be enabled before placing order");
        checkoutPage.placeOrder();
        Assert.assertFalse(checkoutPage.ischeckoutFormdisplayed(), "Checkout form should close after placing order");
    }

    @Test(priority = 10, dataProvider = "userDetails", dataProviderClass = TestDataProvider.class)
    public void testUserDetailsWithDataProvider(String userName, String userEmail) {
        checkoutPage.enterCustomername(userName);
        checkoutPage.enterEmailid(userEmail);
        
        Assert.assertEquals(checkoutPage.getCurrentname(), userName, "Name should match DataProvider input");
        Assert.assertEquals(checkoutPage.getCurrentemailid(), userEmail, "Email should match DataProvider input");
        Assert.assertTrue(checkoutPage.validateForm(userName, userEmail), "Form should be valid with user details");
        
        boolean submitEnabled = checkoutPage.isSubmitButtonEnabled();
        Assert.assertTrue(submitEnabled, "Submit button should be enabled with valid user details");
    }

    @Test(priority = 11, dataProvider = "csvUserData", dataProviderClass = TestDataProvider.class)
    public void testUserDetailsFromCsv(String userName, String userEmail) {
        checkoutPage.enterCustomername(userName);
        checkoutPage.enterEmailid(userEmail);
        
        Assert.assertEquals(checkoutPage.getCurrentname(), userName, "Name should match CSV data");
        Assert.assertEquals(checkoutPage.getCurrentemailid(), userEmail, "Email should match CSV data");
        Assert.assertTrue(checkoutPage.validateForm(userName, userEmail), "Form should be valid with CSV data");
    }
}