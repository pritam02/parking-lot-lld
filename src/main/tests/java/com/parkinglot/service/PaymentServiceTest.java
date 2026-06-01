package com.parkinglot.service;

import com.parkinglot.enums.PaymentMethod;
import com.parkinglot.enums.PaymentStatus;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.model.Vehicle;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Test
    void shouldCalculateFeeForNinetyMinutes() {
        LocalDateTime entry = LocalDateTime.now().minusMinutes(90);
        ParkingTicket ticket = new ParkingTicket(new Vehicle("CAR-1", VehicleType.CAR),
                new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT),
                1, entry);
        PaymentService paymentService = new PaymentService();

        double amount = paymentService.calculateAmountDue(ticket, LocalDateTime.now());

        assertEquals(7.5, amount, 1e-6);
        assertEquals(7.5, ticket.getAmountDue(), 1e-6);
        assertNotNull(ticket.getExitTime());
    }

    @Test
    void shouldReturnFirstHourForZeroOrNegativeDuration() {
        LocalDateTime entry = LocalDateTime.now();
        ParkingTicket ticket = new ParkingTicket(new Vehicle("CAR-1", VehicleType.CAR),
                new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT),
                1, entry);
        PaymentService paymentService = new PaymentService();

        double amount = paymentService.calculateAmountDue(ticket, entry.minusMinutes(5));

        assertEquals(4.0, amount, 1e-6);
    }

    @Test
    void shouldProcessPaymentAndMarkTicketPaid() {
        LocalDateTime entry = LocalDateTime.now().minusHours(1);
        ParkingTicket ticket = new ParkingTicket(new Vehicle("CAR-1", VehicleType.CAR),
                new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT),
                1, entry);
        PaymentService paymentService = new PaymentService();

        paymentService.calculateAmountDue(ticket, LocalDateTime.now());
        assertEquals(PaymentStatus.PENDING, ticket.getPaymentStatus());

        paymentService.processPayment(ticket, PaymentMethod.CASH, true);

        assertTrue(ticket.isPaid());
        assertEquals(PaymentStatus.PAID, ticket.getPaymentStatus());
    }

    @Test
    void shouldThrowWhenPaymentAlreadyPaid() {
        LocalDateTime entry = LocalDateTime.now().minusHours(1);
        ParkingTicket ticket = new ParkingTicket(new Vehicle("CAR-1", VehicleType.CAR),
                new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT),
                1, entry);
        PaymentService paymentService = new PaymentService();

        paymentService.calculateAmountDue(ticket, LocalDateTime.now());
        paymentService.processPayment(ticket, PaymentMethod.CREDIT_CARD, false);

        ParkingLotException exception = assertThrows(ParkingLotException.class,
                () -> paymentService.processPayment(ticket, PaymentMethod.CASH, true));
        assertTrue(exception.getMessage().contains("already paid"));
    }

    @Test
    void shouldThrowWhenAmountNotCalculatedBeforePayment() {
        ParkingTicket ticket = new ParkingTicket(new Vehicle("CAR-1", VehicleType.CAR),
                new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.COMPACT),
                1, LocalDateTime.now());
        PaymentService paymentService = new PaymentService();

        ParkingLotException exception = assertThrows(ParkingLotException.class,
                () -> paymentService.processPayment(ticket, PaymentMethod.CASH, false));
        assertTrue(exception.getMessage().contains("Amount due must be calculated"));
    }
}
