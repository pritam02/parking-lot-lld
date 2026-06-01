package com.parkinglot;

import com.parkinglot.factory.ParkingLotFactory;
import com.parkinglot.enums.PaymentMethod;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.model.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotIntegrationTest {

    @Test
    void shouldCreateParkingLotAndHandleEntryExitFlow() {
        ParkingLot parkingLot = ParkingLotFactory.createDefaultParkingLot();
        assertEquals("Downtown Multi-Floor Parking", parkingLot.getName());
        assertFalse(parkingLot.isFull());

        int availableBefore = parkingLot.getAvailableSpaces();
        ParkingTicket ticket = parkingLot.enter(new Vehicle("INTEG-1", VehicleType.CAR), 0);
        assertNotNull(ticket);
        assertFalse(ticket.isPaid());
        assertEquals(availableBefore - 1, parkingLot.getAvailableSpaces());

        double amount = parkingLot.getAmountDueAtPortal(ticket, ticket.getFloorNumber());
        assertTrue(amount > 0);

        parkingLot.payAtInfoPortal(ticket, ticket.getFloorNumber(), PaymentMethod.CREDIT_CARD);
        assertTrue(ticket.isPaid());

        assertDoesNotThrow(() -> parkingLot.exit(ticket, 0, PaymentMethod.CREDIT_CARD));
        assertEquals(0, parkingLot.getParkingService().getOccupiedCount());
    }

    @Test
    void shouldThrowWhenNoInfoPortalExistsOnFloor() {
        ParkingLot parkingLot = new ParkingLot.Builder()
                .name("Small Lot")
                .addFloor(new com.parkinglot.model.ParkingFloor(1, java.util.Collections.singletonList(
                        new com.parkinglot.model.ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT))))
                .entryPanelCount(1)
                .exitPanelCount(1)
                .build();

        ParkingTicket ticket = parkingLot.enter(new Vehicle("INTEG-2", VehicleType.CAR), 0);
        assertThrows(com.parkinglot.exception.ParkingLotException.class,
                () -> parkingLot.payAtInfoPortal(ticket, 2, PaymentMethod.CASH));
    }
}
