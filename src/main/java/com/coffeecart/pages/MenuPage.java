package com.coffeecart.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.coffeecart.managers.ConfigManager;
import com.coffeecart.utils.LoggerUtil;

/**
 * Handles all interactions with the coffee menu including item selection, pricing, and cart operations.
 * Provides methods for adding items, checking prices, handling promotions, and navigating the menu.
 */
public class MenuPage extends BasePage {

    //WebElement details

    @FindBy(xpath = "//a[normalize-space()='menu']")
    private WebElement menu;

    @FindBy(xpath = "//a[contains(@href, 'cart') or contains(text(), 'cart')]")
    private WebElement cart_Page;

    @FindBy(xpath = "//div[@class='pay-container']")
    private WebElement pay_button;

    @FindBy(xpath = "//a[normalize-space()='github']")
    private WebElement github_Page;

    @FindBy(css = "span.unit-desc")
    private List<WebElement> quantityDisplay;

    @FindBy(css = "div.promo")
    private WebElement specialOfferContainer;

    @FindBy(css = "div[data-v-a68519c8].unit-controller button")
    private List<WebElement> quantityButtons;

    @FindBy(xpath = "//small[contains(text(),'$')]")
    private List<WebElement> allPrices;

    @FindBy(css = "div.cup-body")
    private List<WebElement> menuItems;

    @FindBy(css = "ul h4")
    private List<WebElement> itemNames;

    @FindBy(css = "ul small")
    private List<WebElement> itemPrices;

    @FindBy(xpath = "//div[contains(@class,'ingredient')]")
    private List<WebElement> ingredients;

    @FindBy(xpath = "//div[@class='cup-body']")
    private List<WebElement> allItemname;

    @FindBy(xpath = "//div[@class='cup']")
    private List<WebElement> allImages;

    private final By promoLocator = By.cssSelector("div.promo");

    @FindBy(xpath = "//div[contains(@class, 'promo')]//span")
    private WebElement specialOfferText;

    @FindBy(css = "div.promo .promo-title")
    private WebElement specialOfferTitle;

    @FindBy(css = "div.promo .promo-description")
    private WebElement specialOfferDescription;

    @FindBy(xpath = "//body/div[@id='app']/div[@class='promo']/div[1]")
    private WebElement discountItemImage;

    @FindBy(xpath = "//div[@data-cy='(Discounted)-Mocha']")
    private WebElement discountedItemName;

    @FindBy(xpath = "//div[@data-cy='(Discounted)-Mocha']//div[contains(@class,'ingredient')]")
    private WebElement discountedItemIngredients;

    @FindBy(css = "button[aria-label='Proceed to checkout']")
    private WebElement verifycheckoutButton;

    @FindBy(xpath = "//button[contains(text(),'Total:')]/text()")
    private WebElement verifyPriceincrement;

    @FindBy(xpath = "//p[normalize-space()='No coffee, go add some.']")
    private WebElement emptyCartcheck;

    @FindBy(xpath = "//input[@id='promotion']")
    private WebElement promotionCheckbox;

    @FindBy(xpath = "//button[@id='submit-payment']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[normalize-space()='×']")
    private WebElement closeButton;

    @FindBy(xpath = "//div[@class='promo']")
    private WebElement specialOfferPage;

    @FindBy(xpath = "//div[@class='promo']//span[1]")
    private WebElement specialOfferInfo;

    @FindBy(xpath = "//div[@class='modal-content size']")
    private WebElement payForm;

    @FindBy(xpath = "//input[@id='name']")
    private WebElement nameInput;

    @FindBy(xpath = "//input[@id='email']")
    private WebElement emailInput;

    @FindBy(xpath = "//div[@class='promo']//button[2]")
    private WebElement skipOfferButton;

    @FindBy(xpath = "//button[normalize-space()='Yes, of course!']")
    private WebElement acceptOfferButton;

    @FindBy(css = ".cart-preview .list-item")
    private List<WebElement> previewItems;

    @FindBy(css = ".cart-preview .unit-desc")
    private List<WebElement> previewQuantities;

    @FindBy(css = ".cart-preview")
    private WebElement cartPreviewPopup;

    @FindBy(css ="button[aria-label='Proceed to checkout']")
    private WebElement totalButton;

    By snackbarLocator = By.cssSelector(".snackbar");

    private final By promoTextLocator = By.cssSelector("div.promo .promo-text");

    @FindBy(css = ".unit-controller button[aria-label*='Add']")
    private WebElement increaseQuantity;

    @FindBy(css = ".unit-controller button[aria-label*='Remove']")
    private WebElement decreaseQuantity;

    // Initializes the MenuPage with all web elements and waits
    public MenuPage() {
        super();
    }

    // Refreshes the page and waits for all elements to stabilize
    public void refreshUI() {
        try {
            driver.navigate().refresh();
            wait.until(ExpectedConditions.visibilityOf(menu));
            wait.until(ExpectedConditions.visibilityOfAllElements(menuItems));
            wait.until(ExpectedConditions.elementToBeClickable(pay_button));
            
            try {
                Thread.sleep(ConfigManager.getPageRefreshStabilityWait());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            LoggerUtil.info("Menu page has been refreshed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Failed to refresh the Coffee Cart UI page", e);
            throw e;
        }
    }

    // Navigates to the coffee cart menu page and waits for it to load
    public void navigateToMenuPage() {
        try {
            driver.get(ConfigManager.getBaseUrl());
            waitForElementVisibility(menu);
            LoggerUtil.info("Successfully navigated to coffee cart menu page" + ConfigManager.getBaseUrl());
        } catch (Exception e) {
            LoggerUtil.error("Failed to navigate to the Menu page", e);
            throw e;
        }
    }

    // Checks if the menu navigation link is visible on the page
    public boolean isMenuDisplayed() {
        try {
            return menu.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Verifies if the cart link is displayed and accessible
    public boolean isCartdisplayed() {
        try {
            return cart_Page.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Checks if the GitHub link is visible in the navigation
    public boolean isGithubdisplayed() {
        try {
            return github_Page.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Gets the total amount text from the checkout button
    public String getTotalText() {
        return verifycheckoutButton.getText();
    }


     //Verifies that the total button is enabled and clickable for checkout.
    public boolean isTotalClickable() {
        return verifycheckoutButton.isEnabled();
    }

    // Verifies that every menu item has an associated image displayed
    public boolean verifyAllItemsHaveImages() {
        return allImages.size() == allItemname.size();
    }

    // Gets the menu items list for validation
    public List<WebElement> getMenuItems() {
        wait.until(ExpectedConditions.visibilityOfAllElements(menuItems));
        return menuItems;
    }

    // Validates that all items have prices
    public boolean validateAllItemsHavePrices() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(itemPrices));
            return !itemPrices.isEmpty() && itemPrices.size() == menuItems.size();
        } catch (Exception e) {
            LoggerUtil.error("Failed to validate all items have prices", e);
            return false;
        }
    }

    // Validates that all items have names
    public boolean validateAllItemsHaveNames() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(itemNames));
            return !itemNames.isEmpty() && itemNames.size() == menuItems.size();
        } catch (Exception e) {
            LoggerUtil.error("Failed to validate all items have names", e);
            return false;
        }
    }

    // Validates price range for items
    public boolean validatePriceRange(double minPrice, double maxPrice) {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(itemPrices));
            for (WebElement priceElement : itemPrices) {
                double price = extractPrice(priceElement.getText());
                if (price < minPrice || price > maxPrice) {
                    LoggerUtil.info("Price " + price + " is outside range " + minPrice + "-" + maxPrice);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LoggerUtil.error("Failed to validate price range", e);
            return false;
        }
    }

    // Validates prices above threshold
    public boolean validatePricesAbove(double threshold) {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(itemPrices));
            for (WebElement priceElement : itemPrices) {
                double price = extractPrice(priceElement.getText());
                if (price <= threshold) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LoggerUtil.error("Failed to validate prices above threshold", e);
            return false;
        }
    }

    // Validates price format
    public boolean validatePriceFormat() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(itemPrices));
            for (WebElement priceElement : itemPrices) {
                String priceText = priceElement.getText();
                if (!priceText.matches("\\$\\d+\\.\\d{2}")) {
                    LoggerUtil.info("Invalid price format: " + priceText);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LoggerUtil.error("Failed to validate price format", e);
            return false;
        }
    }

    // Gets all items ingredients
    public Map<String, List<String>> getAllItemsIngredients() {
        try {
            Map<String, List<String>> allIngredients = new HashMap<>();
            wait.until(ExpectedConditions.visibilityOfAllElements(allItemname));
            
            for (WebElement item : allItemname) {
                String itemName = item.getAttribute("aria-label");
                if (itemName != null) {
                    List<String> itemIngredients = getItemIngredients(itemName);
                    allIngredients.put(itemName, itemIngredients);
                }
            }
            
            LoggerUtil.info("Retrieved ingredients for " + allIngredients.size() + " items");
            return allIngredients;
        } catch (Exception e) {
            LoggerUtil.error("Failed to get all items ingredients", e);
            return new HashMap<>();
        }
    }

    // Gets items with specific ingredient count
    public List<String> getItemsWithIngredientsCount(int count, boolean moreThan) {
        try {
            List<String> matchingItems = new ArrayList<>();
            Map<String, List<String>> allIngredients = getAllItemsIngredients();
            
            for (Map.Entry<String, List<String>> entry : allIngredients.entrySet()) {
                int ingredientCount = entry.getValue().size();
                boolean matches = moreThan ? ingredientCount > count : ingredientCount < count;
                if (matches) {
                    matchingItems.add(entry.getKey());
                }
            }
            
            LoggerUtil.info("Found " + matchingItems.size() + " items with " + 
                          (moreThan ? "more than " : "less than ") + count + " ingredients");
            return matchingItems;
        } catch (Exception e) {
            LoggerUtil.error("Failed to get items with ingredient count", e);
            return new ArrayList<>();
        }
    }

    // Adds all items to cart within limit
    public boolean addAllItemsToCart() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(menuItems));
            int maxItems = ConfigManager.getMaxCartItems();
            int itemsAdded = 0;
            
            for (WebElement item : menuItems) {
                if (itemsAdded >= maxItems) {
                    break;
                }
                
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(item));
                    dismissOverlaysIfPresent();
                    clickElement(item);
                    itemsAdded++;
                    waitForCartUpdate();
                } catch (Exception e) {
                    LoggerUtil.error("Failed to add item " + itemsAdded, e);
                }
            }
            
            LoggerUtil.info("Added " + itemsAdded + " items to cart");
            return itemsAdded > 0 && itemsAdded <= maxItems;
        } catch (Exception e) {
            LoggerUtil.error("Failed to add all items to cart", e);
            return false;
        }
    }

    private double extractPrice(String priceText) {
        try {
            String priceValue = priceText.replaceAll("[^0-9.]", "");
            return Double.parseDouble(priceValue);
        } catch (NumberFormatException e) {
            LoggerUtil.error("Failed to parse price: " + priceText);
            throw e;
        }
    }

    // Checks if the shopping cart is empty by examining cart text and total
    public boolean isCartEmpty() {
        try {
            waitForElementVisibility(cart_Page);
            waitForElementVisibility(pay_button);
            String cartText = cart_Page.getText().toLowerCase();
            String payText = pay_button.getText();
            boolean isEmpty = cartText.contains(ConfigManager.getCartEmptyText().toLowerCase()) || payText.contains(ConfigManager.getCartEmptyPrice());
            LoggerUtil.info("Cart empty check - Cart text: '" + cartText + "', Pay text: '" + payText + "', Is empty: " + isEmpty);
            return isEmpty;
        } catch (Exception e) {
            LoggerUtil.error("Error checking if cart is empty", e);
            return false;
        }
    }

    // Adds a specified quantity of a coffee item to the shopping cart
    public void addItemToCart(String itemName, int quantity) {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(menuItems));
            wait.until(ExpectedConditions.elementToBeClickable(pay_button));
            
            WebElement itemToAdd = findItemByName(itemName);
            if (itemToAdd == null) {
                throw new IllegalArgumentException("Item not found: " + itemName);
            }
            wait.until(ExpectedConditions.elementToBeClickable(itemToAdd));
            dismissOverlaysIfPresent();
            clickElement(itemToAdd);
            waitForCartUpdate();
            if (quantity > 1) {
                incrementItemQuantity(quantity - 1);
            }
            LoggerUtil.info("Successfully added " + quantity + " " + itemName + " to the cart");
        } catch (Exception e) {
            LoggerUtil.error("Failed to add item to cart", e);
            throw e;
        }
    }
    
    private WebElement findItemByName(String itemName) {
        for (WebElement item : menuItems) {
            String itemText = item.getAttribute(ConfigManager.getItemAriaLabel());
            if (itemText != null && itemText.toLowerCase(Locale.ROOT).contains(itemName.toLowerCase(Locale.ROOT))) {
                return item;
            }
        }
        return null;
    }
    
    private void dismissOverlaysIfPresent() {
        try {
            // Check for snackbar and wait for it to disappear
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getOverlayDismissWait()));
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(snackbarLocator));
        } catch (Exception e) {
        }
    }
    
    private void waitForCartUpdate() {
        try {
            // Wait for cart to update from empty state
            wait.until(ExpectedConditions.not(
                ExpectedConditions.textToBePresentInElement(pay_button, ConfigManager.getCartEmptyPrice())
            ));
            // Additional wait for DOM stability
            try {
                Thread.sleep(ConfigManager.getDomStabilityWait());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            LoggerUtil.info("Cart update wait completed or timed out");
        }
    }
    
    private void incrementItemQuantity(int increments) {
        try {
            hoverTotalButton();
            wait.until(ExpectedConditions.elementToBeClickable(increaseQuantity));

            for (int i = 0; i < increments; i++) {
                wait.until(ExpectedConditions.elementToBeClickable(increaseQuantity));
                clickElement(increaseQuantity);
                try {
                    Thread.sleep(ConfigManager.getElementInteractionWait());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to increment quantity", e);
        }
    }

    // Adds three items to trigger promotional offers and special deals
    public boolean addThreeItemsForPromo(String itemName) {
        try {
            LoggerUtil.info("Adding 3 " + itemName + " to trigger special promo");
            wait.until(ExpectedConditions.visibilityOfAllElements(menuItems));

            WebElement itemToAdd = findItemByName(itemName);
            if (itemToAdd == null) {
                throw new IllegalArgumentException("Item not found: " + itemName);
            }

            for (int i = 0; i < 3; i++) {
                wait.until(ExpectedConditions.elementToBeClickable(itemToAdd));
                dismissOverlaysIfPresent();
                clickElement(itemToAdd);
                LoggerUtil.info("Added item " + (i + 1) + " of 3");
                waitForCartUpdate();
            }
            return waitForPromoDisplay();
        } catch (Exception e) {
            LoggerUtil.error("Failed to add three items for promo", e);
            return false;
        }
    }
    
    private boolean waitForPromoDisplay() {
        try {
            try {
                Thread.sleep(ConfigManager.getDomStabilityWait());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            WebDriverWait promoWait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getPromoWaitTime()));
            promoWait.until(ExpectedConditions.visibilityOfElementLocated(promoLocator));
            boolean promoDisplayed = isSpecialOfferDisplayed();
            LoggerUtil.info("Special promo displayed: " + promoDisplayed);
            return promoDisplayed;
        } catch (TimeoutException e) {
            LoggerUtil.info("Special promo not displayed after timeout");
            return false;
        }
    }

    /**
     * Checks if the cart preview popup is visible when hovering over the total button.
     * Firefox has known limitations with hover interactions in headless mode.
     */
    public boolean isPreviewPopupVisible() {
        try {
            wait.until(driver -> {
                String totalText = pay_button.getText();
                return !totalText.contains(ConfigManager.getCartEmptyPrice());
            });

            Actions actions = new Actions(driver);
            actions.moveToElement(pay_button).perform();

            By quantityLocator = By.cssSelector("span.unit-desc");
            int timeout = ConfigManager.getBrowser().equalsIgnoreCase("firefox") ? 
                ConfigManager.getFirefoxCartPreviewWait() : ConfigManager.getCartPreviewWait();
            WebDriverWait previewWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            WebElement quantityElement = previewWait.until(
                    ExpectedConditions.visibilityOfElementLocated(quantityLocator));

            boolean isVisible = quantityElement.isDisplayed();
            LoggerUtil.info("Cart preview quantity indicator visible: " + isVisible);

            return isVisible;
        } catch (Exception e) {
            LoggerUtil.error("Preview popup not visible", e);
            return false;
        }
    }

    // Hovers over the total button to display cart preview popup
    public void hoverTotalButton() {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(pay_button).perform();
            LoggerUtil.info("Hovering over Total button");
        } catch (Exception e) {
            LoggerUtil.error("Failed to hover over Total button", e);
            throw e;
        }
    }

    // Clicks the total button to proceed to checkout page
    public void clickTotalButton() {
        try {
            By snackbarLocator = By.cssSelector(".snackbar");
            try {
                WebDriverWait snackbarWait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getSnackbarWaitTime()));
                snackbarWait.until(ExpectedConditions.invisibilityOfElementLocated(snackbarLocator));
            } catch (Exception e) {
                LoggerUtil.info("No snackbar found or already dismissed");
            }
            
            waitForElementClickable(totalButton);
            clickElement(totalButton);
            LoggerUtil.info("Successfully clicked the total button");
        } catch (Exception e) {
            LoggerUtil.error("Failed to click total button", e);
            throw e;
        }
    }

    /**
     * Retrieves the list of ingredients for a specific coffee item.
     * Parses ingredient information from the menu display for validation purposes.
     */
    public List<String> getItemIngredients(String itemName) {
        try {
            List<String> ingredientList = new ArrayList<>();
            for (WebElement item : allItemname) {
                if (item.getAttribute("aria-label").equals(itemName)) {
                    List<WebElement> itemIngredients = ingredients.stream()
                            .filter(ingredient -> ingredient.findElement(By.xpath("..")).equals(item))
                            .collect(Collectors.toList());

                    for (WebElement ingredient : itemIngredients) {
                        ingredientList.add(ingredient.getText().trim());
                    }
                    break;
                }
            }
            LoggerUtil.info("Got ingredients for " + itemName + ": " + ingredientList);
            return ingredientList;
        } catch (Exception e) {
            LoggerUtil.error("Failed to get ingredients for item", e);
            return new ArrayList<>();
        }
    }

    /**
     * Checks if the accept offer button is enabled and clickable.
     * Ensures promotional offers can be accepted when displayed.
     */
    public boolean isAcceptOfferClickable() {
        return acceptOfferButton.isEnabled();
    }

    // Checks if a special promotional offer popup is currently displayed
    public boolean isSpecialOfferDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getPromoWaitTime()));
            WebElement promo = wait.until(ExpectedConditions.visibilityOfElementLocated(promoLocator));
            return promo.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Accepts the promotional offer by clicking the accept button
    public void acceptPromoOffer() {
        try {
            clickElement(acceptOfferButton);
            LoggerUtil.info("Successfully clicked the special offer accept button");
        } catch (Exception e) {
            LoggerUtil.error("Failed to accept promo offer", e);
            throw e;
        }
    }

    // Checks if special offer text is displayed
    public boolean isSpecialOfferTextDisplayed() {
        try {
            return specialOfferText.isDisplayed() && !specialOfferText.getText().trim().isEmpty();
        } catch (Exception e) {
            LoggerUtil.error("Failed to check special offer text", e);
            return false;
        }
    }

    // Checks if special offer image is displayed
    public boolean isSpecialOfferImageDisplayed() {
        try {
            return discountItemImage.isDisplayed();
        } catch (Exception e) {
            LoggerUtil.error("Failed to check special offer image", e);
            return false;
        }
    }

    // Checks if special offer is gone after accepting
    public boolean isSpecialOfferGone() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(promoLocator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}