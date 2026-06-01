package com.parkinglot.panel;

import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.Vehicle;
import com.parkinglot.service.ParkingService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GroundFloorDisplayBoardTest {

    @Test
    void shouldShowFloorStatusForAvailableAndFullLot() {
        ParkingFloor floor = new ParkingFloor(1, Arrays.asList(new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT)));
        ParkingService parkingService = new ParkingService(Arrays.asList(floor));
        GroundFloorDisplayBoard displayBoard = new GroundFloorDisplayBoard(parkingService, Arrays.asList(floor));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(output));
        try {
            displayBoard.showStatus();
            assertTrue(output.toString().contains("Floor 1"));

            output.reset();
            parkingService.parkVehicle(new Vehicle("CAR-1", com.parkinglot.enums.VehicleType.CAR));
            displayBoard.showStatus();
            assertTrue(output.toString().contains("Floor 1"));
        } finally {
            System.setOut(original);
        }
    }
}
