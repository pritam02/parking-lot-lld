package com.parkinglot.panel;

import com.parkinglot.model.DisplayBoard;
import com.parkinglot.model.ParkingFloor;
import com.parkinglot.service.ParkingService;

import java.util.List;

public class GroundFloorDisplayBoard {

    private final ParkingService parkingService;
    private final List<ParkingFloor> floors;

    public GroundFloorDisplayBoard(ParkingService parkingService, List<ParkingFloor> floors) {
        this.parkingService = parkingService;
        this.floors = floors;
    }

    public void showStatus() {
        System.out.println("========== GROUND FLOOR DISPLAY BOARD ==========");
        if (parkingService.isFull()) {
            System.out.println("*** PARKING LOT FULL - NO VACANCY ***");
        } else {
            System.out.println("Available spaces: " + parkingService.getAvailableCount()
                    + " / " + parkingService.getMaxCapacity());
        }
        for (ParkingFloor floor : floors) {
            DisplayBoard board = floor.getDisplayBoard();
            System.out.println(board.getDisplayMessage());
        }
        System.out.println("================================================");
    }
}
