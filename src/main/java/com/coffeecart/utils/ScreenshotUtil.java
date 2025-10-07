package com.coffeecart.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;


import com.coffeecart.managers.ConfigManager;

//Handles automatic screenshot capture for failed tests with timestamped

public class ScreenshotUtil {

    // Captures a screenshot with timestamp and test name for failed test debugging
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            String screenshotPath = ConfigManager.getScreenshotPath();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            String screenshotFileName = testName + "_" + timestamp + ".png";

            File directory = new File(screenshotPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File finalDestination = new File(directory, screenshotFileName);
            FileUtils.copyFile(source, finalDestination);
            String destination = finalDestination.getAbsolutePath();

            LoggerUtil.info("Successfully saved the screenshot: " + destination);
            return destination;

        } catch (IOException e) {
            LoggerUtil.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
