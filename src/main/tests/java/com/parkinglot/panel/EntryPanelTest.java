package com.parkinglot.panel;

import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.Vehicle;
import com.parkinglot.service.ParkingService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EntryPanelTest {

    @Test
    void shouldIssueTicketWhenSpaceIsAvailable() {
        ParkingFloor floor = new ParkingFloor(1, Arrays.asList(new ParkingSpot("S1", ParkingSpotType.COMPACT)));
        ParkingService parkingService = new ParkingService(Arrays.asList(floor));
        EntryPanel entryPanel = new EntryPanel("E1", parkingService);

        assertNotNull(entryPanel.issueTicket(new Vehicle("CAR-1", VehicleType.CAR)));
    }

    @Test
    void shouldThrowWhenParkingIsFull() {
        ParkingFloor floor = new ParkingFloor(1, Arrays.asList(new ParkingSpot("S1", ParkingSpotType.COMPACT)));
        ParkingService parkingService = new ParkingService(Arrays.asList(floor));
        EntryPanel entryPanel = new EntryPanel("E2", parkingService);

        entryPanel.issueTicket(new Vehicle("CAR-1", VehicleType.CAR));
        ParkingLotException exception = assertThrows(ParkingLotException.class,
                () -> entryPanel.issueTicket(new Vehicle("CAR-2", VehicleType.CAR)));
        assertTrue(exception.getMessage().contains("PARKING FULL"));
    }
}
