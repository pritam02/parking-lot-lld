package com.parkinglot.factory;

import com.parkinglot.ParkingLot;
import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.model.ElectricPanel;
import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory to create a sample parking lot configuration for demo and interviews.
 */
public class ParkingLotFactory {

    private ParkingLotFactory() {
    }

    public static ParkingLot createDefaultParkingLot() {
        List<ParkingFloor> floors = new ArrayList<ParkingFloor>();
        floors.add(createFloor(1, 2, 2, 1, 2, 1));
        floors.add(createFloor(2, 3, 2, 1, 1, 2));

        return new ParkingLot.Builder()
                .name("Downtown Multi-Floor Parking")
                .addFloor(floors.get(0))
                .addFloor(floors.get(1))
                .entryPanelCount(2)
                .exitPanelCount(2)
                .build();
    }

    private static ParkingFloor createFloor(int floorNumber,
                                            int compact, int large,
                                            int handicapped, int motorcycle,
                                            int electric) {
        List<ParkingSpot> spots = new ArrayList<ParkingSpot>();
        int index = 1;

        for (int i = 0; i < compact; i++) {
            spots.add(new ParkingSpot("F" + floorNumber + "-C" + index++, ParkingSpotType.COMPACT));
        }
        for (int i = 0; i < large; i++) {
            spots.add(new ParkingSpot("F" + floorNumber + "-L" + index++, ParkingSpotType.LARGE));
        }
        for (int i = 0; i < handicapped; i++) {
            spots.add(new ParkingSpot("F" + floorNumber + "-H" + index++, ParkingSpotType.HANDICAPPED));
        }
        for (int i = 0; i < motorcycle; i++) {
            spots.add(new ParkingSpot("F" + floorNumber + "-M" + index++, ParkingSpotType.MOTORCYCLE));
        }
        for (int i = 0; i < electric; i++) {
            String spotId = "F" + floorNumber + "-E" + index++;
            ElectricPanel panel = new ElectricPanel("EP-" + spotId);
            spots.add(new ParkingSpot(spotId, ParkingSpotType.ELECTRIC, panel));
        }

        return new ParkingFloor(floorNumber, spots);
    }
}
