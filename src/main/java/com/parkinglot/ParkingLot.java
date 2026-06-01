package com.parkinglot;

import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.model.Vehicle;
import com.parkinglot.panel.CustomerInfoPortal;
import com.parkinglot.panel.EntryPanel;
import com.parkinglot.panel.ExitPanel;
import com.parkinglot.panel.GroundFloorDisplayBoard;
import com.parkinglot.service.ParkingService;
import com.parkinglot.service.PaymentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Facade for the parking lot system coordinating entry, exit, payment, and display.
 */
public class ParkingLot {

    private final String name;
    private final ParkingService parkingService;
    private final PaymentService paymentService;
    private final List<EntryPanel> entryPanels;
    private final List<ExitPanel> exitPanels;
    private final Map<Integer, CustomerInfoPortal> infoPortals;
    private final GroundFloorDisplayBoard groundFloorDisplayBoard;
    private final List<ParkingFloor> floors;

    public ParkingLot(String name,
                      List<ParkingFloor> floors,
                      List<EntryPanel> entryPanels,
                      List<ExitPanel> exitPanels,
                      Map<Integer, CustomerInfoPortal> infoPortals,
                      ParkingService parkingService,
                      PaymentService paymentService) {
        this.name = name;
        this.floors = floors;
        this.entryPanels = entryPanels;
        this.exitPanels = exitPanels;
        this.infoPortals = infoPortals;
        this.parkingService = parkingService;
        this.paymentService = paymentService;
        this.groundFloorDisplayBoard = new GroundFloorDisplayBoard(parkingService, floors);
    }

    public String getName() {
        return name;
    }

    public ParkingTicket enter(Vehicle vehicle, int entryPanelIndex) {
        EntryPanel panel = entryPanels.get(entryPanelIndex);
        ParkingTicket ticket = panel.issueTicket(vehicle);
        refreshDisplays();
        return ticket;
    }

    public void exit(ParkingTicket ticket, int exitPanelIndex,
                     com.parkinglot.enums.PaymentMethod method) {
        ExitPanel panel = exitPanels.get(exitPanelIndex);
        panel.processExit(ticket, method);
        refreshDisplays();
    }

    public void payAtInfoPortal(ParkingTicket ticket, int floorNumber,
                                com.parkinglot.enums.PaymentMethod method) {
        CustomerInfoPortal portal = infoPortals.get(floorNumber);
        if (portal == null) {
            throw new com.parkinglot.exception.ParkingLotException("No info portal on floor " + floorNumber);
        }
        portal.payParkingFee(ticket, method);
    }

    public double getAmountDueAtPortal(ParkingTicket ticket, int floorNumber) {
        return infoPortals.get(floorNumber).getAmountDue(ticket);
    }

    public void showDisplayBoards() {
        groundFloorDisplayBoard.showStatus();
        for (ParkingFloor floor : floors) {
            System.out.println(floor.getDisplayBoard());
        }
    }

    public boolean isFull() {
        return parkingService.isFull();
    }

    public int getAvailableSpaces() {
        return parkingService.getAvailableCount();
    }

    public ParkingService getParkingService() {
        return parkingService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public List<ParkingFloor> getFloors() {
        return new ArrayList<ParkingFloor>(floors);
    }

    private void refreshDisplays() {
        parkingService.refreshAllDisplayBoards();
    }

    public static class Builder {

        private String name = "City Parking";
        private List<ParkingFloor> floors = new ArrayList<ParkingFloor>();
        private int entryPanelCount = 2;
        private int exitPanelCount = 2;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder addFloor(ParkingFloor floor) {
            this.floors.add(floor);
            return this;
        }

        public Builder entryPanelCount(int count) {
            this.entryPanelCount = count;
            return this;
        }

        public Builder exitPanelCount(int count) {
            this.exitPanelCount = count;
            return this;
        }

        public ParkingLot build() {
            ParkingService parkingService = new ParkingService(floors);
            PaymentService paymentService = new PaymentService();

            List<EntryPanel> entryPanels = new ArrayList<EntryPanel>();
            for (int i = 1; i <= entryPanelCount; i++) {
                entryPanels.add(new EntryPanel("E" + i, parkingService));
            }

            List<ExitPanel> exitPanels = new ArrayList<ExitPanel>();
            for (int i = 1; i <= exitPanelCount; i++) {
                boolean automated = i % 2 == 1;
                exitPanels.add(new ExitPanel("X" + i, parkingService, paymentService, automated));
            }

            Map<Integer, CustomerInfoPortal> infoPortals = new HashMap<Integer, CustomerInfoPortal>();
            for (ParkingFloor floor : floors) {
                infoPortals.put(floor.getFloorNumber(),
                        new CustomerInfoPortal(floor.getFloorNumber(), paymentService));
            }

            return new ParkingLot(name, floors, entryPanels, exitPanels, infoPortals,
                    parkingService, paymentService);
        }
    }
}
