package com.parkinglot.model;

import com.parkinglot.enums.ParkingSpotType;

import java.util.Optional;

public class ParkingSpot {

    private final String spotId;
    private final ParkingSpotType type;
    private final ElectricPanel electricPanel;
    private Vehicle parkedVehicle;

    public ParkingSpot(String spotId, ParkingSpotType type) {
        this(spotId, type, null);
    }

    public ParkingSpot(String spotId, ParkingSpotType type, ElectricPanel electricPanel) {
        this.spotId = spotId;
        this.type = type;
        this.electricPanel = electricPanel;
    }

    public String getSpotId() {
        return spotId;
    }

    public ParkingSpotType getType() {
        return type;
    }

    public Optional<ElectricPanel> getElectricPanel() {
        return Optional.ofNullable(electricPanel);
    }

    public boolean isAvailable() {
        return parkedVehicle == null;
    }

    public boolean isOccupied() {
        return !isAvailable();
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public void parkVehicle(Vehicle vehicle) {
        if (!isAvailable()) {
            throw new IllegalStateException("Spot " + spotId + " is already occupied");
        }
        this.parkedVehicle = vehicle;
        if (electricPanel != null && vehicle.getType() == com.parkinglot.enums.VehicleType.ELECTRIC_CAR) {
            electricPanel.startCharging();
        }
    }

    public void unparkVehicle() {
        if (electricPanel != null) {
            electricPanel.stopCharging();
        }
        this.parkedVehicle = null;
    }

    @Override
    public String toString() {
        return spotId + " [" + type + "] " + (isAvailable() ? "FREE" : "OCCUPIED");
    }
}
