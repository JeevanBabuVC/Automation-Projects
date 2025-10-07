package com.coffeecart.listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.coffeecart.tests.BaseTest;
import com.coffeecart.utils.LoggerUtil;
import com.coffeecart.utils.ScreenshotUtil;

import io.qameta.allure.Attachment;

/**
 * Listens to TestNG events and handles logging, screenshots, and reporting for test execution.
 * Automatically captures screenshots on failures and provides detailed execution summaries.
 */
public class TestListener implements ITestListener {

    // Logs when a test suite begins execution
    @Override
    public void onStart(ITestContext context) {
        LoggerUtil.info("Test Suite has been started: " + context.getName());
    }

    // Logs when an individual test method starts and records start time
    @Override
    public void onTestStart(ITestResult result) {
        LoggerUtil.info("Test has been started: " + result.getName());
        result.setAttribute("startTime", System.currentTimeMillis());
        

    }

    // Logs when a test passes successfully and calculates execution duration
    @Override
    public void onTestSuccess(ITestResult result) {
        LoggerUtil.info("Test Passed: " + result.getName());
        long duration = calculateDuration(result);

    }

    // Handles test failures by logging errors and capturing screenshots for debugging
    @Override
    public void onTestFailure(ITestResult result) {
        LoggerUtil.error("Test Failed: " + result.getName());
        long duration = calculateDuration(result);
        
        try {
            Object testClass = result.getInstance();
            WebDriver driver = ((BaseTest) testClass).getDriver();

            if (driver != null) {
                attachScreenshot(driver);
                String screenshotPath = ScreenshotUtil.captureScreenshot(
                        driver,
                        result.getName()
                );
                LoggerUtil.info("Screenshot has been saved successfully: " + screenshotPath);
            }
            

            
        } catch (Exception e) {
            LoggerUtil.error("Failed to capture the screenshot: " + e.getMessage());
        }
    }

    // Logs when a test is skipped due to dependencies or conditions
    @Override
    public void onTestSkipped(ITestResult result) {
        LoggerUtil.warn("Test has been skipped: " + result.getName());
    }

    // Provides test execution summary when the entire test suite completes
    @Override
    public void onFinish(ITestContext context) {
        LoggerUtil.info("Test Suite has been Finished: " + context.getName());
        
        int total = context.getAllTestMethods().length;
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        
        double passPercentage = total > 0 ? (double) passed / total * 100 : 0;
        
        LoggerUtil.info(String.format("Test Execution Summary - Total: %d, Passed: %d (%.2f%%), Failed: %d, Skipped: %d", 
                total, passed, passPercentage, failed, skipped));
    }
    
    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] attachScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
    
    // Calculates how long a test took to execute in milliseconds
    private long calculateDuration(ITestResult result) {
        Long startTime = (Long) result.getAttribute("startTime");
        return startTime != null ? System.currentTimeMillis() - startTime : 0;
    }
    
    // Creates a descriptive string with class and method name for logging
    private String getTestDescription(ITestResult result) {
        String className = result.getTestClass().getName();
        String methodName = result.getMethod().getMethodName();
        
        if (className.contains("MenuTests")) {
            return "Testing coffee menu functionality and item interactions";
        } else if (className.contains("CartTests")) {
            return "Testing shopping cart operations and navigation";
        } else if (className.contains("CheckoutTests")) {
            return "Testing checkout process and form validations";
        } else if (className.contains("EndToEndTests")) {
            return "Testing complete user workflow from menu to order completion";
        }
        return "Automated test for " + methodName;
    }
}
