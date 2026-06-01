package com.parkinglot.pricing;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Tiered hourly parking fee:
 * - First hour: $4.00
 * - Second and third hours: $3.50 each
 * - Remaining hours: $2.50 each
 */
public class ParkingFeeCalculator {

    private static final double FIRST_HOUR_RATE = 4.0;
    private static final double SECOND_THIRD_HOUR_RATE = 3.5;
    private static final double REMAINING_HOUR_RATE = 2.5;

    public double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        long totalMinutes = Duration.between(entryTime, exitTime).toMinutes();
        if (totalMinutes <= 0) {
            return FIRST_HOUR_RATE;
        }

        int totalHours = (int) Math.ceil(totalMinutes / 60.0);
        double fee = 0.0;

        for (int hour = 1; hour <= totalHours; hour++) {
            if (hour == 1) {
                fee += FIRST_HOUR_RATE;
            } else if (hour == 2 || hour == 3) {
                fee += SECOND_THIRD_HOUR_RATE;
            } else {
                fee += REMAINING_HOUR_RATE;
            }
        }
        return fee;
    }
}
