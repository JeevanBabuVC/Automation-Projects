package com.coffeecart.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


 //Centralized logging utility that provides consistent logging across the entire test framework.

public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    // Logs informational messages for test progress and successful operations
    public static void info(String message) {
        logger.info(message);
    }

    // Logs error messages when something goes wrong during test execution
    public static void error(String message) {
        logger.error(message);
    }

    // Logs warning messages for potential issues that don't stop test execution
    public static void warn(String message) {
        logger.warn(message);
    }

    // Logs  debug information for troubleshooting test issues
    public static void debug(String message) {
        logger.debug(message);
    }

    // Logs error messages with full exception details for better debugging
    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
