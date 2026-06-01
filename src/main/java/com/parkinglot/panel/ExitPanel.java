package com.parkinglot.panel;

import com.parkinglot.enums.PaymentMethod;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.service.ParkingService;
import com.parkinglot.service.PaymentService;

import java.time.LocalDateTime;

public class ExitPanel {

    private final String panelId;
    private final ParkingService parkingService;
    private final PaymentService paymentService;
    private final boolean automated;

    public ExitPanel(String panelId, ParkingService parkingService, PaymentService paymentService, boolean automated) {
        this.panelId = panelId;
        this.parkingService = parkingService;
        this.paymentService = paymentService;
        this.automated = automated;
    }

    public String getPanelId() {
        return panelId;
    }

    public void processExit(ParkingTicket ticket, PaymentMethod method) {
        if (ticket.isPaid()) {
            System.out.println("[ExitPanel-" + panelId + "] Ticket already paid. Allowing exit.");
            parkingService.unparkVehicle(ticket);
            return;
        }

        paymentService.calculateAmountDue(ticket, LocalDateTime.now());
        paymentService.processPayment(ticket, method, !automated);
        parkingService.unparkVehicle(ticket);
        System.out.println("[ExitPanel-" + panelId + "] Exit granted for " + ticket.getVehicle());
    }

    public void processExitWithoutPayment(ParkingTicket ticket) {
        if (!ticket.isPaid()) {
            throw new ParkingLotException("Payment required at exit for ticket " + ticket.getTicketId());
        }
        parkingService.unparkVehicle(ticket);
        System.out.println("[ExitPanel-" + panelId + "] Exit granted (pre-paid) for " + ticket.getVehicle());
    }
}
