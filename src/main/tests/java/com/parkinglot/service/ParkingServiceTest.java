package com.parkinglot.service;

import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.model.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ParkingServiceTest {

    @Test
    void shouldParkCarInPreferredCompactSpot() {
        ParkingFloor floor = new ParkingFloor(1, Arrays.asList(
                new ParkingSpot("S1", ParkingSpotType.COMPACT),
                new ParkingSpot("S2", ParkingSpotType.LARGE)
        ));
        ParkingService service = new ParkingService(Arrays.asList(floor));
        Vehicle car = new Vehicle("CAR-1", VehicleType.CAR);

        ParkingTicket ticket = service.parkVehicle(car);

        assertEquals(1, ticket.getFloorNumber());
        assertEquals(ParkingSpotType.COMPACT, ticket.getSpot().getType());
        assertEquals(1, service.getOccupiedCount());
        assertEquals(2, service.getMaxCapacity());
        assertEquals(1, service.getAvailableCount());
    }

    @Test
    void shouldThrowWhenParkingLotIsFull() {
        ParkingFloor floor = new ParkingFloor(1, Arrays.asList(new ParkingSpot("S1", ParkingSpotType.COMPACT)));
        ParkingService service = new ParkingService(Arrays.asList(floor));
        service.parkVehicle(new Vehicle("CAR-1", VehicleType.CAR));

        ParkingLotException exception = assertThrows(ParkingLotException.class,
                () -> service.parkVehicle(new Vehicle("CAR-2", VehicleType.CAR)));
        assertTrue(exception.getMessage().contains("Parking lot is full"));
    }

    @Test
    void shouldRejectElectricCarWhenNoElectricSpotExists() {
        ParkingFloor floor = new ParkingFloor(1, Arrays.asList(new ParkingSpot("S1", ParkingSpotType.COMPACT)));
        ParkingService service = new ParkingService(Arrays.asList(floor));

        ParkingLotException exception = assertThrows(ParkingLotException.class,
                () -> service.parkVehicle(new Vehicle("ELEC-1", VehicleType.ELECTRIC_CAR)));
        assertTrue(exception.getMessage().contains("No suitable spot available"));
    }

    @Test
    void shouldUnparkVehicleAndRefreshFloorState() {
        ParkingSpot spot = new ParkingSpot("S1", ParkingSpotType.COMPACT);
        ParkingFloor floor = new ParkingFloor(1, Arrays.asList(spot));
        ParkingService service = new ParkingService(Arrays.asList(floor));
        ParkingTicket ticket = service.parkVehicle(new Vehicle("CAR-1", VehicleType.CAR));

        service.unparkVehicle(ticket);

        assertTrue(spot.isAvailable());
        assertEquals(0, floor.getOccupiedCount());
        assertEquals(1, service.getAvailableCount());
    }
}
