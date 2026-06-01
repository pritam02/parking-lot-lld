package com.parkinglot.model;

import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.enums.VehicleType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ParkingFloorTest {

    @Test
    void shouldFindAvailableSpotByType() {
        List<ParkingSpot> spots = Arrays.asList(
                new ParkingSpot("S1", ParkingSpotType.COMPACT),
                new ParkingSpot("S2", ParkingSpotType.MOTORCYCLE)
        );
        ParkingFloor floor = new ParkingFloor(1, spots);

        Optional<ParkingSpot> compact = floor.findAvailableSpot(ParkingSpotType.COMPACT);
        Optional<ParkingSpot> motorcycle = floor.findAvailableSpot(ParkingSpotType.MOTORCYCLE);
        Optional<ParkingSpot> large = floor.findAvailableSpot(ParkingSpotType.LARGE);

        assertTrue(compact.isPresent());
        assertEquals("S1", compact.get().getSpotId());
        assertTrue(motorcycle.isPresent());
        assertFalse(large.isPresent());
    }

    @Test
    void shouldRefreshDisplayBoardAfterParkingChanges() {
        ParkingSpot compact = new ParkingSpot("S1", ParkingSpotType.COMPACT);
        ParkingSpot motorcycle = new ParkingSpot("S2", ParkingSpotType.MOTORCYCLE);
        ParkingFloor floor = new ParkingFloor(2, Arrays.asList(compact, motorcycle));

        assertEquals(1, floor.getDisplayBoard().getFreeCount(ParkingSpotType.COMPACT));
        assertEquals(1, floor.getDisplayBoard().getFreeCount(ParkingSpotType.MOTORCYCLE));

        compact.parkVehicle(new Vehicle("CAR-1", VehicleType.CAR));
        floor.refreshDisplayBoard();

        assertEquals(0, floor.getDisplayBoard().getFreeCount(ParkingSpotType.COMPACT));
        assertEquals(1, floor.getDisplayBoard().getFreeCount(ParkingSpotType.MOTORCYCLE));
        assertEquals(2, floor.getTotalCapacity());
        assertEquals(1, floor.getOccupiedCount());
    }
}
