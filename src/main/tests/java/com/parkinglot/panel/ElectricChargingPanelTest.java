package com.parkinglot.panel;

import com.parkinglot.enums.PaymentMethod;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.model.ElectricPanel;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.ParkingTicket;
import com.parkinglot.model.Vehicle;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ElectricChargingPanelTest {

    @Test
    void shouldStartAndStopChargingForElectricTicket() {
        ElectricPanel electricPanel = new ElectricPanel("EP-1");
        ParkingSpot spot = new ParkingSpot("S1", com.parkinglot.enums.ParkingSpotType.ELECTRIC, electricPanel);
        ParkingTicket ticket = new ParkingTicket(new Vehicle("ELEC-1", VehicleType.ELECTRIC_CAR), spot, 1, LocalDateTime.now().minusMinutes(30));
        ElectricChargingPanel panel = new ElectricChargingPanel("EP1");

        panel.payAndStartCharging(ticket, PaymentMethod.CASH, 12.5);
        assertTrue(electricPanel.isInUse());

        panel.stopCharging(ticket);
        assertFalse(electricPanel.isInUse());
    }
}
