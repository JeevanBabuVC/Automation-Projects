package com.coffeecart.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads configuration properties from external files to support flexible test configuration.
 */
public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "resources/config.properties";

    static {
        loadProperties();
    }


     //Loads properties from the configuration file into memory.
    private static void loadProperties() {
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties = new Properties();
            properties.load(fileInputStream);
            LoggerUtil.info("Configuration properties has been loaded successfully");
        } catch (IOException e) {
            LoggerUtil.error("Failed to load config file: " + e.getMessage());
            throw new RuntimeException("Error in loading the config file", e);
        }
    }

    //Retrieves a string property value by key.
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in the config file");
        }
        return value.trim();
    }


     //Retrieves a string property value by key with a default fallback value.
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue).trim();
    }


     //Retrieves an integer property value by key.
    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }


      //Retrieves a boolean property value by key.
    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
}