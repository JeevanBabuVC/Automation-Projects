package com.coffeecart.tests;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.coffeecart.data.TestDataProvider;
import com.coffeecart.listeners.TestListener;
import com.coffeecart.managers.ConfigManager;
import com.coffeecart.managers.DriverManager;
import com.coffeecart.pages.MenuPage;
import com.coffeecart.utils.LoggerUtil;

//Test the functionalities related to menu page

//Tests coffee menu functionality including item display, pricing, selection, and cart interactions.

@Listeners(TestListener.class)
public class MenuTests extends BaseTest {
    private MenuPage menuPage;


    @BeforeMethod
    public void setup() {
        menuPage = new MenuPage();
    }
    

    private void resetCartIfNeeded() {
        try {
            if (!menuPage.isCartEmpty()) {
                menuPage.refreshUI();
                LoggerUtil.info("Cart reset before test");
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to reset cart", e);
        }
    }
    

    @AfterClass
    public void classCleanup() {
        try {
            // Close browser to ensure clean state for next test class
            DriverManager.closeDriver();
            driver = null;
            LoggerUtil.info("Browser closed after MenuTests to ensure clean state");
        } catch (Exception e) {
            LoggerUtil.error("Failed to close browser after MenuTests", e);
        }
    }

    //To check menu page is loaded
    @Test(priority = 1, description = "Verify page loads with all basic elements", groups = {"smoke"})
    public void testPageLoaded() {
        Assert.assertTrue(menuPage.isMenuDisplayed(), "Menu is not displayed");
        Assert.assertTrue(menuPage.isCartdisplayed(), "Cart is not displayed");
        Assert.assertTrue(menuPage.isGithubdisplayed(), "Github link is not displayed");
    }

    //To validate only 3 items in a row
    @Test(priority = 2, description = "Verify menu items are arranged in groups of three")
    public void testThreeItemsInRow() {
        List<WebElement> menuItems = menuPage.getMenuItems();
        Assert.assertTrue(menuItems.size() % 3 == 0, "Items are not arranged in groups of 3");
    }


    //To validate the item price 
    @Test(priority = 3, description = "Verify all items have prices")
    public void testAllItemsHavePrices() {
        Assert.assertTrue(menuPage.validateAllItemsHavePrices(), "Not all items have prices");
    }

    //check all items has name
    @Test(priority = 4, description = "Verify all items have names")
    public void testAllItemsHaveNames() {
        Assert.assertTrue(menuPage.validateAllItemsHaveNames(), "Not all items have names");
    }

    //check all items has images
    @Test(priority = 5, description = "Verify all items have images")
    public void testAllItemsHaveImages() {
        Assert.assertTrue(menuPage.verifyAllItemsHaveImages(), "Not all items have images");
    }


    //Verify the price range (boundary limits)
    @Test(priority = 6, description = "Verify items price range", groups = {"non-functional"})
    public void testItemPriceRange() {
        double minPrice = ConfigManager.getPriceRangeMin();
        double maxPrice = ConfigManager.getPriceRangeMax();
        Assert.assertTrue(menuPage.validatePriceRange(minPrice, maxPrice),
                "Items prices are not within expected range of $" + minPrice + "-$" + maxPrice);
    }

    //check if any item between $20 tp $30
    @Test(priority = 7, description = "Verify items between $20-$30 - Expected to fail")
    public void testItemsBetweenTwentyAndThirty() {
        double minPrice = ConfigManager.getInvalidPriceRangeMin();
        double maxPrice = ConfigManager.getInvalidPriceRangeMax();
        Assert.assertTrue(menuPage.validatePriceRange(minPrice, maxPrice),
                "Items should be between $" + minPrice + " and $" + maxPrice);
    }

    //Check if any item price is above $30
    @Test(priority = 8, description = "Verify items above $30 - Expected to fail")
    public void testItemsAboveThirty() {
        double threshold = ConfigManager.getInvalidPriceThreshold();
        Assert.assertTrue(menuPage.validatePricesAbove(threshold), "Items should be above $" + threshold);
    }

    //Check the price format
    @Test(priority = 9, description = "Verify price format")
    public void testPriceFormat() {
        Assert.assertTrue(menuPage.validatePriceFormat(), "Prices are not in valid format");
    }

    //Get all ingredient details
    @Test(priority = 10, description = "Verify all items have ingredients")
    public void testGetAllIngredients() {
        Map<String, List<String>> ingredients = menuPage.getAllItemsIngredients();
        Assert.assertFalse(ingredients.isEmpty(), "No ingredients found");
    }

    //Check the items with less than 2 ingredients in the menu page
    @Test(priority = 11, description = "Verify items with less than 2 ingredients")
    public void testItemsWithLessThanTwoIngredients() {
        List<String> items = menuPage.getItemsWithIngredientsCount(2, false);
        Assert.assertFalse(items.isEmpty(), "No items found with less than 2 ingredients");
    }

    //Verify the items with more than 2 ingredients
    @Test(priority = 12, description = "Verify items with more than 2 ingredients")
    public void testItemsWithMoreThanTwoIngredients() {
        List<String> items = menuPage.getItemsWithIngredientsCount(2, true);
        Assert.assertFalse(items.isEmpty(), "No items found with more than 2 ingredients");
    }

    //Check total option is showing in menu page
    @Test(priority = 13, description = "Verify total option availability", groups = {"smoke"})
    public void testTotalOptionAvailable() {
        Assert.assertTrue(menuPage.isTotalClickable(), "Total option is not available");
    }

    //Add special coffee item to the cart
    @Test(priority = 14, description = "Verify adding specific coffee items to cart", dataProvider = "coffeeData", dataProviderClass = TestDataProvider.class)
    public void testAddSpecificCoffeeToCart(String coffeeName, int quantity, double expectedPrice) {
        resetCartIfNeeded();
        menuPage.addItemToCart(coffeeName, quantity);
        
        try {
            Thread.sleep(ConfigManager.getCartUpdateWait());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertFalse(menuPage.isCartEmpty(), "Cart should not be empty after adding " + coffeeName);
        System.out.println("Testing: " + coffeeName + " with quantity " + quantity + " and expected price $" + expectedPrice);
    }

    //Validate using data provider
    @Test(priority = 15, description = "Test coffee items from DataProvider", dataProvider = "coffeeData", dataProviderClass = TestDataProvider.class)
    public void testCoffeeFromDataProvider(String coffeeName, int quantity, double expectedPrice) {
        resetCartIfNeeded();
        menuPage.addItemToCart(coffeeName, quantity);
        
        try {
            Thread.sleep(ConfigManager.getCartUpdateWait());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Assert.assertFalse(menuPage.isCartEmpty(), "Cart should not be empty after adding " + coffeeName);
        System.out.println("Testing coffee: " + coffeeName + " with quantity: " + quantity + " and expected price: $" + expectedPrice);
    }

    //Check the special promo offer is displaying
    @Test(priority = 16, description = "Verify special promo text and image", groups = {"non-functional"})
    public void testSpecialPromoDisplay() {
        resetCartIfNeeded();
        String promoItem = ConfigManager.getPromoTriggerItem();
        Assert.assertTrue(menuPage.addThreeItemsForPromo(promoItem),
                "Failed to trigger special promo");
        Assert.assertTrue(menuPage.isSpecialOfferTextDisplayed(),
                "Special promo text not displayed correctly");
        Assert.assertTrue(menuPage.isSpecialOfferImageDisplayed(),
                "Special promo Mocha image not displayed");
    }

    //Verify if the offer is able to accept
    @Test(priority = 17, description = "Verify special promo acceptance")
    public void testSpecialPromoAcceptance() {
        resetCartIfNeeded();
        String promoItem = ConfigManager.getPromoTriggerItem();
        Assert.assertTrue(menuPage.addThreeItemsForPromo(promoItem), "Failed to trigger special promo");
        Assert.assertTrue(menuPage.isAcceptOfferClickable(), "Accept offer button not clickable");
        menuPage.acceptPromoOffer();
        Assert.assertTrue(menuPage.isSpecialOfferGone(), "Special promo still displayed after accepting");
    }

    //Rest the cart after refresh to use for other methods
    @Test(priority = 18, description = "Verify cart resets after refresh")
    public void testCartResetAfterRefresh() {
        resetCartIfNeeded();
        String testItem = ConfigManager.getTestCoffeeItem();
        int testQuantity = 1;
        menuPage.addItemToCart(testItem, testQuantity);
        menuPage.refreshUI();
        Assert.assertTrue(menuPage.isCartEmpty(), "Cart not reset after refresh");
    }

    //Verify if all items can be added to cart
    @Test(priority = 19, description = "Verify adding items to cart", groups = {"functional"})
    public void testAddAllItems() {
        resetCartIfNeeded();
        boolean result = menuPage.addAllItemsToCart();
        Assert.assertTrue(result,
                "Cart should contain items within the maximum limit");
    }

}
