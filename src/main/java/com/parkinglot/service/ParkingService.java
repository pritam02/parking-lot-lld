package com.parkinglot.service;

import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.model.Vehicle;
import com.parkinglot.strategy.SpotAllocationStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ParkingService {

    private final List<ParkingFloor> floors;
    private final int maxCapacity;

    public ParkingService(List<ParkingFloor> floors) {
        this.floors = floors;
        this.maxCapacity = calculateTotalCapacity();
    }

    private int calculateTotalCapacity() {
        int total = 0;
        for (ParkingFloor floor : floors) {
            total += floor.getTotalCapacity();
        }
        return total;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getOccupiedCount() {
        int count = 0;
        for (ParkingFloor floor : floors) {
            count += floor.getOccupiedCount();
        }
        return count;
    }

    public boolean isFull() {
        return getOccupiedCount() >= maxCapacity;
    }

    public int getAvailableCount() {
        return maxCapacity - getOccupiedCount();
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) {
        if (isFull()) {
            throw new ParkingLotException("Parking lot is full. Cannot park vehicle " + vehicle.getLicensePlate());
        }

        List<ParkingSpotType> preferredTypes = SpotAllocationStrategy.getPreferredSpotTypes(vehicle.getType());

        for (ParkingSpotType spotType : preferredTypes) {
            for (ParkingFloor floor : floors) {
                Optional<ParkingSpot> spotOpt = floor.findAvailableSpot(spotType);
                if (spotOpt.isPresent()) {
                    ParkingSpot spot = spotOpt.get();
                    if (vehicle.getType() == VehicleType.ELECTRIC_CAR && spot.getType() != ParkingSpotType.ELECTRIC) {
                        continue;
                    }
                    spot.parkVehicle(vehicle);
                    floor.refreshDisplayBoard();
                    return new ParkingTicket(vehicle, spot, floor.getFloorNumber(), LocalDateTime.now());
                }
            }
        }

        throw new ParkingLotException("No suitable spot available for " + vehicle.getType()
                + " (" + vehicle.getLicensePlate() + ")");
    }

    public void unparkVehicle(ParkingTicket ticket) {
        ParkingSpot spot = ticket.getSpot();
        for (ParkingFloor floor : floors) {
            if (floor.getFloorNumber() == ticket.getFloorNumber()) {
                spot.unparkVehicle();
                floor.refreshDisplayBoard();
                return;
            }
        }
        spot.unparkVehicle();
    }

    public void refreshAllDisplayBoards() {
        for (ParkingFloor floor : floors) {
            floor.refreshDisplayBoard();
        }
    }
}
