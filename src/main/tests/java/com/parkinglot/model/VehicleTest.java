package com.parkinglot.model;

import com.parkinglot.enums.VehicleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    void shouldCompareVehiclesByLicensePlate() {
        Vehicle first = new Vehicle("ABC-123", VehicleType.CAR);
        Vehicle second = new Vehicle("ABC-123", VehicleType.CAR);
        Vehicle third = new Vehicle("XYZ-789", VehicleType.CAR);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first, third);
        assertFalse(first.equals(null));
        assertTrue(first.toString().contains("ABC-123"));
    }
}
