package com.parkinglot.strategy;

import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.enums.VehicleType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpotAllocationStrategy {

    private SpotAllocationStrategy() {
    }

    public static List<ParkingSpotType> getPreferredSpotTypes(VehicleType vehicleType) {
        switch (vehicleType) {
            case MOTORCYCLE:
                return Collections.singletonList(ParkingSpotType.MOTORCYCLE);
            case ELECTRIC_CAR:
                return Arrays.asList(ParkingSpotType.ELECTRIC, ParkingSpotType.COMPACT);
            case CAR:
                return Arrays.asList(ParkingSpotType.COMPACT, ParkingSpotType.LARGE);
            case VAN:
                return Arrays.asList(ParkingSpotType.LARGE, ParkingSpotType.COMPACT);
            case TRUCK:
                return Collections.singletonList(ParkingSpotType.LARGE);
            default:
                return Collections.singletonList(ParkingSpotType.COMPACT);
        }
    }
}
