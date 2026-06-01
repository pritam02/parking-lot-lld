package com.parkinglot.model;

import com.parkinglot.enums.ParkingSpotType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParkingFloor {

    private final int floorNumber;
    private final List<ParkingSpot> spots;
    private final DisplayBoard displayBoard;

    public ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<ParkingSpot>(spots);
        this.displayBoard = new DisplayBoard(floorNumber);
        refreshDisplayBoard();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return new ArrayList<ParkingSpot>(spots);
    }

    public DisplayBoard getDisplayBoard() {
        return displayBoard;
    }

    public Optional<ParkingSpot> findAvailableSpot(ParkingSpotType spotType) {
        for (ParkingSpot spot : spots) {
            if (spot.getType() == spotType && spot.isAvailable()) {
                return Optional.of(spot);
            }
        }
        return Optional.empty();
    }

    public void refreshDisplayBoard() {
        Map<ParkingSpotType, Integer> counts = new EnumMap<ParkingSpotType, Integer>(ParkingSpotType.class);
        for (ParkingSpotType type : ParkingSpotType.values()) {
            counts.put(type, 0);
        }
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable()) {
                counts.put(spot.getType(), counts.get(spot.getType()) + 1);
            }
        }
        for (Map.Entry<ParkingSpotType, Integer> entry : counts.entrySet()) {
            displayBoard.updateAvailability(entry.getKey(), entry.getValue());
        }
    }

    public int getTotalCapacity() {
        return spots.size();
    }

    public int getOccupiedCount() {
        int count = 0;
        for (ParkingSpot spot : spots) {
            if (spot.isOccupied()) {
                count++;
            }
        }
        return count;
    }
}
