package dk.cphbusiness.webscraping;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Purpose: Show how to read data from file
 *
 * @author: Thomas Hartmann
 */
public class ReadFlightData {
    ObjectMapper om = new ObjectMapper();

    public void readFile() {
        String fileName = "multiflight.json";
        File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
        try {
            FileReader fr = new FileReader(file);
            FlightStatusResponse flightDTO = om.readValue(fr, FlightStatusResponse.class);
            System.out.println(flightDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) {
        ReadFlightData rfd = new ReadFlightData();
        rfd.readFile();
    }


    @Getter
    @Setter
    private static class FlightStatusResponse {
        private Pagination pagination;
        private List<FlightData> data;
        @Override
        public String toString() {
            return data.toString();
        }
    }


    @Getter
    @Setter
    private static class Pagination {
        private int limit;
        private int offset;
        private int count;
        private int total;
    }

    @Getter
    @Setter
    @ToString
    private static class FlightData {
        private String flight_date;
        private String flight_status;
        private Departure departure;
        private Arrival arrival;
        private Airline airline;
        private Flight flight;
        private Aircraft aircraft;
        private Live live;
    }

    @Getter
    @Setter
    @ToString
    private static class Departure {
        private String airport;
        private String timezone;
        private String iata;
        private String icao;
        private String terminal;
        private String gate;
        private int delay;
        private String scheduled;
        private String estimated;
        private String actual;
        private String estimated_runway;
        private String actual_runway;

    }

    @Getter
    @Setter
    @ToString
    private static class Arrival {
        private String airport;
        private String timezone;
        private String iata;
        private String icao;
        private String terminal;
        private String gate;
        private String baggage;
        private int delay;
        private String scheduled;
        private String estimated;
        private String actual;
        private String estimated_runway;
        private String actual_runway;

    }

    @Getter
    @Setter
    @ToString
    private static class Airline {
        private String name;
        private String iata;
        private String icao;

    }

    @Getter
    @Setter
    @ToString
    private static class Flight {
        private String number;
        private String iata;
        private String icao;
        private Object codeshared; // You might want to replace Object with the appropriate type

    }

    @Getter
    @Setter
    @ToString
    private static class Aircraft {
        private String registration;
        private String iata;
        private String icao;
        private String icao24;

    }

    @Getter
    @Setter
    @ToString
    private static class Live {
        private boolean ground;
        private String updated;
        private double latitude;
        private double longitude;
        private double altitude;
        private double direction;
        private double speed_horizontal;
        private double speed_vertical;
    }
}
