package com.parkinglot.panel;

import com.parkinglot.enums.PaymentMethod;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.service.PaymentService;

import java.time.LocalDateTime;

public class CustomerInfoPortal {

    private final int floorNumber;
    private final PaymentService paymentService;

    public CustomerInfoPortal(int floorNumber, PaymentService paymentService) {
        this.floorNumber = floorNumber;
        this.paymentService = paymentService;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public double getAmountDue(ParkingTicket ticket) {
        validateTicketOnFloor(ticket);
        return paymentService.calculateAmountDue(ticket, LocalDateTime.now());
    }

    public void payParkingFee(ParkingTicket ticket, PaymentMethod method) {
        validateTicketOnFloor(ticket);
        if (ticket.getAmountDue() <= 0) {
            paymentService.calculateAmountDue(ticket, LocalDateTime.now());
        }
        paymentService.processPayment(ticket, method, false);
        System.out.println("[InfoPortal-Floor" + floorNumber + "] Payment complete. "
                + "You may exit without paying at the exit gate.");
    }

    private void validateTicketOnFloor(ParkingTicket ticket) {
        if (ticket.getFloorNumber() != floorNumber) {
            throw new ParkingLotException("Ticket " + ticket.getTicketId()
                    + " belongs to floor " + ticket.getFloorNumber()
                    + ", not floor " + floorNumber);
        }
    }
}
