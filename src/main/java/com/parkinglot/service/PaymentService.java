package com.parkinglot.service;

import com.parkinglot.enums.PaymentMethod;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.pricing.ParkingFeeCalculator;

import java.time.LocalDateTime;

public class PaymentService {

    private final ParkingFeeCalculator feeCalculator;

    public PaymentService() {
        this.feeCalculator = new ParkingFeeCalculator();
    }

    public double calculateAmountDue(ParkingTicket ticket, LocalDateTime exitTime) {
        double fee = feeCalculator.calculateFee(ticket.getEntryTime(), exitTime);
        ticket.setExitTime(exitTime);
        ticket.setAmountDue(fee);
        return fee;
    }

    public void processPayment(ParkingTicket ticket, PaymentMethod method, boolean viaAttendant) {
        if (ticket.isPaid()) {
            throw new ParkingLotException("Ticket " + ticket.getTicketId() + " is already paid");
        }
        if (ticket.getAmountDue() <= 0) {
            throw new ParkingLotException("Amount due must be calculated before payment");
        }
        ticket.markPaid();
        String channel = viaAttendant ? "attendant" : "automated panel";
        System.out.printf("Payment of $%.2f received via %s (%s) for ticket %s%n",
                ticket.getAmountDue(), method, channel, ticket.getTicketId());
    }
}
