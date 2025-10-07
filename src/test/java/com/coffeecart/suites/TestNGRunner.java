package com.coffeecart.suites;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

//Programmatic TestNG runner that allows running test suites from Java code instead of command line.

public class TestNGRunner {
    public static void main(String[] args) {
        TestNG testNG = new TestNG();
        List<String> suites = new ArrayList<>();
        suites.add("src/test/resources/testng.xml");
        testNG.setTestSuites(suites);
        testNG.run();
    }
}
