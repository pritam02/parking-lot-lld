package com.parkinglot.model;

import com.parkinglot.enums.ParkingSpotType;

import java.util.EnumMap;
import java.util.Map;

public class DisplayBoard {

    private final int floorNumber;
    private final Map<ParkingSpotType, Integer> freeSpotsByType;

    public DisplayBoard(int floorNumber) {
        this.floorNumber = floorNumber;
        this.freeSpotsByType = new EnumMap<ParkingSpotType, Integer>(ParkingSpotType.class);
        for (ParkingSpotType type : ParkingSpotType.values()) {
            freeSpotsByType.put(type, 0);
        }
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void updateAvailability(ParkingSpotType type, int freeCount) {
        freeSpotsByType.put(type, freeCount);
    }

    public int getFreeCount(ParkingSpotType type) {
        return freeSpotsByType.getOrDefault(type, 0);
    }

    public String getDisplayMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Floor ").append(floorNumber).append(" - Available spots: ");
        boolean first = true;
        for (Map.Entry<ParkingSpotType, Integer> entry : freeSpotsByType.entrySet()) {
            if (entry.getValue() > 0) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
        }
        if (first) {
            sb.append("NONE");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getDisplayMessage();
    }
}
