package com.coffeecart.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


 //Handles different data formats including user data, coffee data, and test constants.

public class ExcelReader {
    private static final String CSV_FILE_PATH = "resources/testdata.csv";


     //Retrieves test data from CSV file for the specified sheet name.
    public static Object[][] getTestdata(String sheetName) {
        try {
            return readCsvData(sheetName);
        } catch (Exception e) {
            LoggerUtil.error("Failed to read CSV data: " + e.getMessage());
            throw new RuntimeException("No test data available for: " + sheetName, e);
        }
    }

     // Reads CSV data for a specific sheet and converts it to the appropriate format.
    private static Object[][] readCsvData(String sheetName) throws IOException {
        List<Object[]> dataList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean inTargetSheet = false;
            boolean headerRead = false;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("# " + sheetName)) {
                    inTargetSheet = true;
                    headerRead = false;
                    continue;
                } else if (line.startsWith("#")) {
                    inTargetSheet = false;
                    continue;
                }
                
                if (inTargetSheet) {
                    if (!headerRead) {
                        headerRead = true; // Skip header row
                        continue;
                    }
                    String[] values = line.split(",");
                    if (sheetName.equals("UserData")) {
                        dataList.add(new Object[]{values[0].trim(), values[1].trim()});
                    } else if (sheetName.equals("CoffeeData")) {
                        dataList.add(new Object[]{values[0].trim(), Integer.parseInt(values[1].trim()), Double.parseDouble(values[2].trim())});
                    } else if (sheetName.equals("TestConstants")) {
                        dataList.add(new Object[]{values[0].trim(), values[1].trim()});
                    }
                }
            }
        }
        
        if (dataList.isEmpty()) {
            throw new RuntimeException("No data has been found for sheet: " + sheetName);
        }
        
        return dataList.toArray(new Object[0][]);
    }
    

     //Reads CSV data and returns it as a list of maps for more flexible data access.
    public static List<Map<String, String>> getTestDataAsMaps(String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new RuntimeException("CSV file is empty");
            }
            String[] headers = headerLine.split(",");
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> rowData = new HashMap<>();
                
                for (int i = 0; i < Math.min(headers.length, values.length); i++) {
                    rowData.put(headers[i].trim(), values[i].trim());
                }
                dataList.add(rowData);
            }
        } catch (IOException e) {
            LoggerUtil.error("Failed to read CSV as map: " + e.getMessage());
            throw new RuntimeException("No map data is available", e);
        }
        
        return dataList;
    }
    

}