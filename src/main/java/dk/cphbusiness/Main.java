package dk.cphbusiness;

import java.time.Duration;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        long d = Duration.between (                  // Represent a span of time of hours, minutes, seconds.
                        LocalTime.MIN ,                 // 00:00:00
                        LocalTime.parse ( "02:30" )  // Parse text as a time-of-day.
                )                                   // Returns a `Duration` object, a span-of-time.
                .toMinutes();                        // Generate a `String` with text in standard ISO 8601 format.
        System.out.println(d);

    }
}