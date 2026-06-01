package com.parkinglot.panel;

import com.parkinglot.enums.PaymentMethod;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ElectricPanel;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.ParkingTicket;

public class ElectricChargingPanel {

    private final String panelId;

    public ElectricChargingPanel(String panelId) {
        this.panelId = panelId;
    }

    public void payAndStartCharging(ParkingTicket ticket, PaymentMethod method, double chargingFee) {
        ParkingSpot spot = ticket.getSpot();
        ElectricPanel electricPanel = spot.getElectricPanel()
                .orElseThrow(() -> new ParkingLotException("Spot " + spot.getSpotId() + " has no electric panel"));

        if (!electricPanel.isInUse()) {
            electricPanel.startCharging();
        }

        System.out.printf("[ElectricPanel-%s] Charging fee $%.2f paid via %s for spot %s%n",
                panelId, chargingFee, method, spot.getSpotId());
    }

    public void stopCharging(ParkingTicket ticket) {
        ticket.getSpot().getElectricPanel().ifPresent(ElectricPanel::stopCharging);
        System.out.println("[ElectricPanel-" + panelId + "] Charging stopped for spot "
                + ticket.getSpot().getSpotId());
    }
}
