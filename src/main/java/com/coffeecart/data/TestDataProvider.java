package com.coffeecart.data;

import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

import com.coffeecart.utils.ExcelReader;

/**
 * Provides test data for data-driven testing across different test scenarios.
 */
public class TestDataProvider {

    // Returns coffee items with their quantities and prices for menu testing
    @DataProvider(name = "coffeeData")
    public static Object[][] fetchCoffeeData() {
        return new Object[][] {
                { "Espresso", 1, 10.00 },
                { "Espresso con Panna", 1, 14.00 },
                { "Flat White", 1, 18.00 }
        };
    }

    // Provides sample usernames and email address for checkout form testing
    @DataProvider(name = "userDetails")
    public static Object[][] fetchUserDetails() {
        return new Object[][] {
                { "Jeevan Babu", "jeevan@example.com" },
                { "Vin Diesel", "vindiesel@example.com" }
        };
    }

    // Supplies additional test users that simulate CSV data for form validation
    @DataProvider(name = "csvUserData")
    public static Object[][] fetchCsvUserData() {
        return new Object[][] {
                { "Jeevan Babu", "jeevan@test.com" },
                { "Vin Diesel", "vindiesel@test.com" },
                { "Amit Kumar", "amit@test.com" }
        };
    }

    // This method loads coffee data from Excel file for more complex test scenarios
    @DataProvider(name = "excelCoffeeData")
    public static Object[][] fetchExcelCoffeeData() {
        return ExcelReader.getTestdata("CoffeeData");
    }

    // This method retrieves user data from Excel for comprehensive user testing
    @DataProvider(name = "excelUserData")
    public static Object[][] fetchExcelUserData() {
        return ExcelReader.getTestdata("UserData");
    }

    // Used for converting Excel data into a more flexible map format for complex data handling
    // Added this method for future use case
    public static List<Map<String, String>> fetchTestDataFromExcel(String sheetName) {
        return ExcelReader.getTestDataAsMaps(sheetName);
    }
}
