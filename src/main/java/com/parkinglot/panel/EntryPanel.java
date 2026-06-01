package com.parkinglot.panel;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.model.Vehicle;
import com.parkinglot.service.ParkingService;

public class EntryPanel {

    private final String panelId;
    private final ParkingService parkingService;

    public EntryPanel(String panelId, ParkingService parkingService) {
        this.panelId = panelId;
        this.parkingService = parkingService;
    }

    public String getPanelId() {
        return panelId;
    }

    public ParkingTicket issueTicket(Vehicle vehicle) {
        if (parkingService.isFull()) {
            String message = "PARKING FULL - No entry allowed. Please try again later.";
            System.out.println("[EntryPanel-" + panelId + "] " + message);
            throw new ParkingLotException(message);
        }
        ParkingTicket ticket = parkingService.parkVehicle(vehicle);
        System.out.println("[EntryPanel-" + panelId + "] Ticket issued: " + ticket);
        System.out.println("[EntryPanel-" + panelId + "] Please proceed to Floor "
                + ticket.getFloorNumber() + ", Spot " + ticket.getSpot().getSpotId());
        return ticket;
    }
}
