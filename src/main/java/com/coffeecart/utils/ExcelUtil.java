package com.coffeecart.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


 // Reads test data from CSV files to support data-driven testing scenarios.

public class ExcelUtil {
    
    // Reads test data from CSV file and returns it as a key-value map for the specified test case
    public static Map<String, String> getTestData(String filePath, String sheetName, String testCaseName) {
        Map<String, String> testData = new HashMap<>();
        String csvPath = filePath.replace(".xlsx", ".csv");
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            boolean inTestCasesSheet = false;
            String[] headers = null;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) continue;
                
                if (line.startsWith("# TestCases")) {
                    inTestCasesSheet = true;
                    continue;
                } else if (line.startsWith("#")) {
                    inTestCasesSheet = false;
                    continue;
                }
                
                if (inTestCasesSheet) {
                    if (headers == null) {
                        headers = line.split(",");
                        continue;
                    }
                    
                    String[] values = line.split(",");
                    if (values.length > 0 && values[0].trim().equals(testCaseName)) {
                        for (int i = 0; i < Math.min(headers.length, values.length); i++) {
                            testData.put(headers[i].trim(), values[i].trim());
                        }
                        break;
                    }
                }
            }
            
        } catch (IOException e) {
            LoggerUtil.error("Failed to read CSV file: " + e.getMessage());
            throw new RuntimeException("CSV file not found: " + csvPath, e);
        }
        
        return testData;
    }
}