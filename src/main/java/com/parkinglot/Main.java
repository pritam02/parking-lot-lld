package com.parkinglot;

import com.parkinglot.enums.PaymentMethod;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.factory.ParkingLotFactory;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.model.Vehicle;
import com.parkinglot.panel.ElectricChargingPanel;
import com.parkinglot.pricing.ParkingFeeCalculator;

import java.time.LocalDateTime;

/**
 * Demo driver simulating parking lot flows for machine coding interviews.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Parking Lot System Demo ===\n");

        ParkingLot parkingLot = ParkingLotFactory.createDefaultParkingLot();
        System.out.println("Initialized: " + parkingLot.getName());
        System.out.println("Capacity: " + parkingLot.getParkingService().getMaxCapacity() + " spots\n");

        parkingLot.showDisplayBoards();
        System.out.println();

        demonstrateParkingAndExit(parkingLot);
        demonstrateInfoPortalPayment(parkingLot);
        demonstrateElectricCharging(parkingLot);
        demonstrateFullParkingLot(parkingLot);
        demonstrateFeeCalculation();

        System.out.println("\n=== Demo Complete ===");
    }

    private static void demonstrateParkingAndExit(ParkingLot parkingLot) throws InterruptedException {
        System.out.println("--- Scenario 1: Park and pay at exit (credit card) ---");
        Vehicle car = new Vehicle("ABC-1234", VehicleType.CAR);
        ParkingTicket ticket = parkingLot.enter(car, 0);
        Thread.sleep(100);
        parkingLot.exit(ticket, 0, PaymentMethod.CREDIT_CARD);
        System.out.println();
    }

    private static void demonstrateInfoPortalPayment(ParkingLot parkingLot) throws InterruptedException {
        System.out.println("--- Scenario 2: Pay at floor info portal, exit without payment ---");
        Vehicle van = new Vehicle("VAN-9999", VehicleType.VAN);
        ParkingTicket ticket = parkingLot.enter(van, 1);
        Thread.sleep(100);

        double amount = parkingLot.getAmountDueAtPortal(ticket, ticket.getFloorNumber());
        System.out.println("Amount due at info portal: $" + String.format("%.2f", amount));

        parkingLot.payAtInfoPortal(ticket, ticket.getFloorNumber(), PaymentMethod.CASH);
        parkingLot.exit(ticket, 0, PaymentMethod.CASH);
        System.out.println();
    }

    private static void demonstrateElectricCharging(ParkingLot parkingLot) {
        System.out.println("--- Scenario 3: Electric car with charging panel ---");
        Vehicle ev = new Vehicle("EV-2024", VehicleType.ELECTRIC_CAR);
        ParkingTicket ticket = parkingLot.enter(ev, 0);

        ElectricChargingPanel chargingPanel = new ElectricChargingPanel("CHG-1");
        chargingPanel.payAndStartCharging(ticket, PaymentMethod.CREDIT_CARD, 15.0);

        parkingLot.exit(ticket, 1, PaymentMethod.CREDIT_CARD);
        chargingPanel.stopCharging(ticket);
        System.out.println();
    }

    private static void demonstrateFullParkingLot(ParkingLot parkingLot) {
        System.out.println("--- Scenario 4: Fill parking lot and show full message ---");
        int panelIndex = 0;
        VehicleType[] types = {VehicleType.CAR, VehicleType.TRUCK, VehicleType.MOTORCYCLE,
                VehicleType.VAN, VehicleType.CAR, VehicleType.TRUCK};
        int i = 0;
        while (!parkingLot.isFull()) {
            Vehicle v = new Vehicle("FILL-" + i, types[i % types.length]);
            parkingLot.enter(v, panelIndex % 2);
            panelIndex++;
            i++;
        }

        parkingLot.showDisplayBoards();

        try {
            parkingLot.enter(new Vehicle("OVERFLOW", VehicleType.CAR), 0);
        } catch (ParkingLotException e) {
            System.out.println("Entry blocked: " + e.getMessage());
        }
        System.out.println();
    }

    private static void demonstrateFeeCalculation() {
        System.out.println("--- Scenario 5: Tiered hourly fee calculation ---");
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();
        LocalDateTime entry = LocalDateTime.now().minusHours(5).minusMinutes(30);

        double fee = calculator.calculateFee(entry, LocalDateTime.now());
        System.out.println("Parking duration ~5.5 hours => Fee: $" + String.format("%.2f", fee));
        System.out.println("  Hour 1: $4.00 | Hours 2-3: $3.50 each | Hours 4+: $2.50 each");
        System.out.println("  Expected: 4 + 3.5 + 3.5 + 2.5 + 2.5 + 2.5 = $18.50");
    }
}
