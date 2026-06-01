package com.parkinglot.model;

import com.parkinglot.enums.ParkingSpotType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisplayBoardTest {

    @Test
    void shouldShowNoneWhenNoSpotsAreAvailable() {
        DisplayBoard board = new DisplayBoard(1);

        assertEquals("Floor 1 - Available spots: NONE", board.getDisplayMessage());
        assertEquals("Floor 1 - Available spots: NONE", board.toString());
    }

    @Test
    void shouldUpdateAvailabilityForMultipleSpotTypes() {
        DisplayBoard board = new DisplayBoard(2);
        board.updateAvailability(ParkingSpotType.COMPACT, 3);
        board.updateAvailability(ParkingSpotType.ELECTRIC, 1);

        assertEquals(3, board.getFreeCount(ParkingSpotType.COMPACT));
        assertEquals(1, board.getFreeCount(ParkingSpotType.ELECTRIC));
        assertTrue(board.getDisplayMessage().contains("COMPACT=3"));
        assertTrue(board.getDisplayMessage().contains("ELECTRIC=1"));
    }
}
