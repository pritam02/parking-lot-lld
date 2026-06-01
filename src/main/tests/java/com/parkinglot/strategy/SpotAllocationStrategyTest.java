package com.parkinglot.strategy;

import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.enums.VehicleType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SpotAllocationStrategyTest {

    @Test
    void shouldReturnPreferredSpotTypesForAllVehicleTypes() {
        assertEquals(Arrays.asList(ParkingSpotType.MOTORCYCLE), SpotAllocationStrategy.getPreferredSpotTypes(VehicleType.MOTORCYCLE));
        assertEquals(Arrays.asList(ParkingSpotType.ELECTRIC, ParkingSpotType.COMPACT), SpotAllocationStrategy.getPreferredSpotTypes(VehicleType.ELECTRIC_CAR));
        assertEquals(Arrays.asList(ParkingSpotType.COMPACT, ParkingSpotType.LARGE), SpotAllocationStrategy.getPreferredSpotTypes(VehicleType.CAR));
        assertEquals(Arrays.asList(ParkingSpotType.LARGE, ParkingSpotType.COMPACT), SpotAllocationStrategy.getPreferredSpotTypes(VehicleType.VAN));
        assertEquals(Arrays.asList(ParkingSpotType.LARGE), SpotAllocationStrategy.getPreferredSpotTypes(VehicleType.TRUCK));
    }
}
