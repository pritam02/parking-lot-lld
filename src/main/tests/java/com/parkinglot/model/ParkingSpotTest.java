package com.parkinglot.model;

import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.enums.VehicleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotTest {

    @Test
    void shouldBeAvailableWhenCreated() {
        ParkingSpot spot = new ParkingSpot("S1", ParkingSpotType.COMPACT);
        assertTrue(spot.isAvailable());
        assertFalse(spot.isOccupied());
        assertEquals("S1", spot.getSpotId());
        assertEquals(ParkingSpotType.COMPACT, spot.getType());
    }

    @Test
    void shouldParkVehicleAndMarkOccupied() {
        Vehicle vehicle = new Vehicle("ABC-123", VehicleType.CAR);
        ParkingSpot spot = new ParkingSpot("S1", ParkingSpotType.COMPACT);

        spot.parkVehicle(vehicle);

        assertFalse(spot.isAvailable());
        assertTrue(spot.isOccupied());
        assertSame(vehicle, spot.getParkedVehicle());
        assertEquals("S1 [COMPACT] OCCUPIED", spot.toString());
    }

    @Test
    void shouldStartChargingWhenElectricCarParksInElectricSpot() {
        Vehicle vehicle = new Vehicle("ELEC-1", VehicleType.ELECTRIC_CAR);
        ElectricPanel panel = new ElectricPanel("EP-S1");
        ParkingSpot spot = new ParkingSpot("S1", ParkingSpotType.ELECTRIC, panel);

        spot.parkVehicle(vehicle);

        assertTrue(spot.isOccupied());
        assertTrue(panel.isInUse());
    }

    @Test
    void shouldThrowWhenParkingIntoOccupiedSpot() {
        Vehicle first = new Vehicle("ABC-123", VehicleType.CAR);
        Vehicle second = new Vehicle("XYZ-789", VehicleType.CAR);
        ParkingSpot spot = new ParkingSpot("S1", ParkingSpotType.COMPACT);
        spot.parkVehicle(first);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> spot.parkVehicle(second));
        assertTrue(exception.getMessage().contains("already occupied"));
    }

    @Test
    void shouldStopChargingWhenUnparking() {
        Vehicle vehicle = new Vehicle("ELEC-1", VehicleType.ELECTRIC_CAR);
        ElectricPanel panel = new ElectricPanel("EP-S1");
        ParkingSpot spot = new ParkingSpot("S1", ParkingSpotType.ELECTRIC, panel);
        spot.parkVehicle(vehicle);

        spot.unparkVehicle();

        assertTrue(spot.isAvailable());
        assertFalse(panel.isInUse());
        assertNull(spot.getParkedVehicle());
    }
}
