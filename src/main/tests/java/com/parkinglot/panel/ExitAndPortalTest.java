package com.parkinglot.panel;

import com.parkinglot.enums.PaymentMethod;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.model.Vehicle;
import com.parkinglot.service.ParkingService;
import com.parkinglot.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ExitAndPortalTest {

    @Test
    void shouldAllowExitWhenTicketAlreadyPaid() {
        ParkingSpot spot = new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT);
        ParkingFloor floor = new ParkingFloor(1, Arrays.asList(spot));
        ParkingService parkingService = new ParkingService(Arrays.asList(floor));
        PaymentService paymentService = new PaymentService();
        ParkingTicket ticket = parkingService.parkVehicle(new Vehicle("CAR-1", VehicleType.CAR));
        paymentService.calculateAmountDue(ticket, ticket.getEntryTime().plusMinutes(30));
        paymentService.processPayment(ticket, PaymentMethod.CASH, true);

        ExitPanel exitPanel = new ExitPanel("X1", parkingService, paymentService, true);
        assertDoesNotThrow(() -> exitPanel.processExit(ticket, PaymentMethod.CASH));
        assertTrue(spot.isAvailable());
    }

    @Test
    void shouldRequirePaymentBeforeExitWithoutPrePaidTicket() {
        ParkingFloor floor = new ParkingFloor(1, Arrays.asList(new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT)));
        ParkingService parkingService = new ParkingService(Arrays.asList(floor));
        PaymentService paymentService = new PaymentService();
        ParkingTicket ticket = parkingService.parkVehicle(new Vehicle("CAR-2", VehicleType.CAR));
        ExitPanel exitPanel = new ExitPanel("X2", parkingService, paymentService, false);

        ParkingLotException exception = assertThrows(ParkingLotException.class,
                () -> exitPanel.processExitWithoutPayment(ticket));
        assertTrue(exception.getMessage().contains("Payment required"));
    }

    @Test
    void shouldProvideAmountAndPayAtCustomerInfoPortal() {
        ParkingSpot spot = new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT);
        ParkingFloor floor = new ParkingFloor(3, Arrays.asList(spot));
        PaymentService paymentService = new PaymentService();
        CustomerInfoPortal infoPortal = new CustomerInfoPortal(3, paymentService);
        ParkingService parkingService = new ParkingService(Arrays.asList(floor));
        ParkingTicket ticket = parkingService.parkVehicle(new Vehicle("CAR-3", VehicleType.CAR));

        double amount = infoPortal.getAmountDue(ticket);
        assertTrue(amount > 0);
        assertEquals(amount, ticket.getAmountDue(), 1e-6);

        infoPortal.payParkingFee(ticket, PaymentMethod.CREDIT_CARD);
        assertTrue(ticket.isPaid());
    }

    @Test
    void shouldThrowWhenPayingOnWrongFloorPortal() {
        ParkingFloor floor = new ParkingFloor(4, Arrays.asList(new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT)));
        PaymentService paymentService = new PaymentService();
        CustomerInfoPortal portal = new CustomerInfoPortal(5, paymentService);
        ParkingService parkingService = new ParkingService(Arrays.asList(floor));
        ParkingTicket ticket = parkingService.parkVehicle(new Vehicle("CAR-4", VehicleType.CAR));

        ParkingLotException exception = assertThrows(ParkingLotException.class,
                () -> portal.getAmountDue(ticket));
        assertTrue(exception.getMessage().contains("belongs to floor"));
    }
}
