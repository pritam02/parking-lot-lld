package com.parkinglot.model;

import com.parkinglot.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class ParkingTicket {

    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final int floorNumber;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double amountDue;
    private PaymentStatus paymentStatus;

    public ParkingTicket(Vehicle vehicle, ParkingSpot spot, int floorNumber, LocalDateTime entryTime) {
        this.ticketId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.vehicle = vehicle;
        this.spot = spot;
        this.floorNumber = floorNumber;
        this.entryTime = entryTime;
        this.paymentStatus = PaymentStatus.PENDING;
        this.amountDue = 0.0;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void markPaid() {
        this.paymentStatus = PaymentStatus.PAID;
    }

    public boolean isPaid() {
        return paymentStatus == PaymentStatus.PAID;
    }

    @Override
    public String toString() {
        return "Ticket{" + ticketId + ", vehicle=" + vehicle + ", floor=" + floorNumber
                + ", spot=" + spot.getSpotId() + ", status=" + paymentStatus + ", amount=$" + amountDue + '}';
    }
}
