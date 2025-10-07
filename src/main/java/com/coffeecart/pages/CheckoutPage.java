package com.coffeecart.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.coffeecart.utils.LoggerUtil;

/**
 * Handles checkout form interactions including customer details entry, validation, and order submission.
 */
public class CheckoutPage extends BasePage {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)\\.(com|org|net)$";

    @FindBy(xpath = "//button[normalize-space()='×']")
    private WebElement closeButton;

    @FindBy(xpath = "//input[@id='email']")
    private WebElement emailidInput;

    @FindBy(css = ".pay-container")
    private WebElement total;

    @FindBy(xpath = "//button[text()='Submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//div[@class='modal-content size']")
    private WebElement paymentForm;

    @FindBy(xpath = "//input[@id='promotion']")
    private WebElement promotionalCheckbox;

    @FindBy(xpath = "//input[@id='name']")
    private WebElement nameInput;

    // Enters the customer name
    public void enterCustomername(String name) {
        try {
            sendKeys(nameInput, name);
            LoggerUtil.info("Successfully entered the customer name: " + name);
        } catch (Exception e) {
            LoggerUtil.error("Failed to enter the customer name", e);
            throw e;
        }
    }

    // Enters the email address into the checkout form email field
    public void enterEmailid(String email) {
        try {
            sendKeys(emailidInput, email);
            boolean isValid = isValidEmailAddress(email);
            LoggerUtil.info("Successfully entered the email address: " + email + " - Valid format: " + isValid);
        } catch (Exception e) {
            LoggerUtil.error("Failed to enter the email address", e);
            throw e;
        }
    }

    // Checks if the promotional offer checkbox is currently selected
    public boolean isPromoOfferSelected() {
        return promotionalCheckbox.isSelected();
    }

    // Toggles the promotional offer checkbox on or off
    public void togglePromoOffer() {
        try {
            clickElement(promotionalCheckbox);
            LoggerUtil.info("Successfully toggled the promotional offer");
        } catch (Exception e) {
            LoggerUtil.error("Failed to toggle the promotional offer", e);
            throw e;
        }
    }

    // Verifies if the checkout form modal is currently displayed
    public boolean ischeckoutFormdisplayed() {
        return isWebElementDisplayed(paymentForm);
    }

    // Closes the checkout form by clicking the close button
    public void closeCheckout() {
        try {
            clickElement(closeButton);
            LoggerUtil.info("Successfully closed the checkout form");
        } catch (Exception e) {
            LoggerUtil.error("Faced error in closing the checkout form", e);
            throw e;
        }
    }

    // Checks if both name and email fields are empty
    public boolean isEmptyField() {
        try {
            return nameInput.getAttribute("value").isEmpty() && emailidInput.getAttribute("value").isEmpty();
        } catch (Exception e) {
            LoggerUtil.error("Failed to check whether the field is empty or not", e);
            return true;
        }
    }

    // Gets the total amount displayed in the checkout form
    public String getTotalAmount() {
        try {
            String totalText = total.getText();
            LoggerUtil.info("Successfully retrieved the total amount: " + totalText);
            return totalText;
        } catch (Exception e) {
            LoggerUtil.error("Failed to get total amount", e);
            return "";
        }
    }


     //Checks if the checkout form is in its initial empty state.

    public boolean isFormInInitialState() {
        try {
            boolean nameEmpty = getCurrentname().isEmpty();
            boolean emailEmpty = getCurrentemailid().isEmpty();
            boolean promoUnchecked = !isPromoOfferSelected();
            boolean initialState = nameEmpty && emailEmpty && promoUnchecked;
            LoggerUtil.info("Verify form initial state check - Name empty: " + nameEmpty + ", Email empty: " + emailEmpty + ", Promo unchecked: " + promoUnchecked + ", Is in initial state: " + initialState);
            return initialState;
        } catch (Exception e) {
            LoggerUtil.error("Failed to check the form initial state", e);
            return false;
        }
    }

    // Fills the entire payment form with name, email, optional promo selection
    public void fillPaymentForm(String name, String email, boolean clickPromoOffer) {
        try {
            enterCustomername(name);
            enterEmailid(email);
            if (clickPromoOffer != isPromoOfferSelected()) {
                togglePromoOffer();
            }
            LoggerUtil.info("Successfully filled checkout with name: " + name + " and email: " + email);
        } catch (Exception e) {
            LoggerUtil.error("Error in filling the payment form", e);
            throw e;
        }
    }


    // Checks if the submit button is enabled and clickable
    public boolean isSubmitButtonEnabled() {
        try {
            waitForElementVisible(submitButton);
            boolean nameNotEmpty = !getCurrentname().isEmpty();
            boolean emailNotEmpty = !getCurrentemailid().isEmpty();
            boolean emailValid = isValidEmailAddress(getCurrentemailid());
            return nameNotEmpty && emailNotEmpty && emailValid;
        } catch (Exception e) {
            LoggerUtil.error("Failed to check submit button state", e);
            return false;
        }
    }


     //Gets the current value entered in the name input field.
    public String getCurrentname() {
        try {
            return nameInput.getAttribute("value");
        } catch (Exception e) {
            LoggerUtil.error("Failed to fetch the name in form", e);
            return "";
        }
    }

    //Get current email address and return actual email address
    public String getCurrentemailid() {
        try {
            return emailidInput.getAttribute("value");
        } catch (Exception e) {
            LoggerUtil.error("Failed to fetch the email address entered in the form", e);
            return "";
        }
    }

    // Validates if the provided email address has correct format and structure
    public boolean isValidEmailAddress(String email) {
        try {
            boolean hasAtSymbol = email.contains("@");
            boolean hasDot = email.contains(".");
            boolean correctOrder = email.indexOf("@") < email.lastIndexOf(".");
            boolean matchesPattern = email.matches(EMAIL_PATTERN);

            LoggerUtil.info("Email validation check - Has @: " + hasAtSymbol + ", Has dot: " + hasDot + ", Correct order: " + correctOrder + ", Matches pattern: " + matchesPattern);

            return hasAtSymbol && hasDot && correctOrder && matchesPattern;
        } catch (Exception e) {
            LoggerUtil.error("Failed to validate email format", e);
            return false;
        }
    }

    // Validates the entire form by checking name and email against expected values
    public boolean validateForm(String expectedName, String expectedEmail) {
        try {
            String currentName = getCurrentname();
            String currentEmail = getCurrentemailid();

            boolean nameValid = !currentName.isEmpty();
            boolean emailValid = isValidEmailAddress(currentEmail);

            if (expectedName != null && expectedEmail != null) {
                nameValid = nameValid && currentName.equals(expectedName);
                emailValid = emailValid && currentEmail.equals(expectedEmail);
            }

            boolean formValid = nameValid && emailValid;

            LoggerUtil.info("Form validation - Name valid: " + nameValid + ", Email valid: " + emailValid + ", Form valid: " + formValid + 
                    (expectedName != null ? ", Name match: " + currentName.equals(expectedName) : "") + 
                    (expectedEmail != null ? ", Email match: " + currentEmail.equals(expectedEmail) : ""));

            return formValid;
        } catch (Exception e) {
            LoggerUtil.error("Failed to validate form", e);
            return false;
        }
    }

    /**
     * Validates the form with current values without comparing to expected values.
     * Checks that name is not empty and email has valid format.
     */
    public boolean validateForm() {
        return validateForm(null, null);
    }

    // Submits the order by clicking the submit button after form validation
    public void placeOrder() {
        try {
            if (validateForm()) {
                clickElement(submitButton);
                wait.until(ExpectedConditions.invisibilityOf(paymentForm));
                LoggerUtil.info("Successfully placed the order - Form is valid");
            } else {
                LoggerUtil.info("Cannot place the order - Form validation failed");
                throw new IllegalStateException("Cannot submit the form - validation failed");
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to place order", e);
            throw e;
        }
    }
}