package com.parkinglot;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void shouldRunDemoAndPrintScenarioMarkers() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        try {
            assertDoesNotThrow(() -> Main.main(new String[0]));
        } finally {
            System.setOut(originalOut);
        }

        String console = output.toString();
        assertTrue(console.contains("=== Parking Lot System Demo ==="));
        assertTrue(console.contains("--- Scenario 1: Park and pay at exit (credit card) ---"));
        assertTrue(console.contains("--- Scenario 2: Pay at floor info portal, exit without payment ---"));
        assertTrue(console.contains("--- Scenario 3: Electric car with charging panel ---"));
        assertTrue(console.contains("--- Scenario 4: Fill parking lot and show full message ---"));
        assertTrue(console.contains("--- Scenario 5: Tiered hourly fee calculation ---"));
        assertTrue(console.contains("=== Demo Complete ==="));
    }
}
