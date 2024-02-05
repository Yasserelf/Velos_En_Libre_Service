package main;

import java.util.*;

import ControlCenter.*;
import Exceptions.*;

import Station.Station;
import Rentable.Bike;
import Rentable.ClassicBike;
import Rentable.ElectricBike;
import Rentable.Rentable;

/**
 * The main class to initialize and simulate the bike-sharing system.
 * It creates stations, adds rentable bikes, and starts the simulation.
 */
public class Main {

    /**
     * The main method to execute the bike-sharing system simulation.
     *
     * @param args Command-line arguments (not used in this application).
     * @throws InvalidAccomodationCapacityException If an invalid accommodation capacity is encountered.
     * @throws FullStationException                If a station is full.
     * @throws EmptySpaceException                 If there is an empty space issue.
     * @throws OutOfServiceException               If a rentable object is out of service.
     * @throws AlreadyRentedException              If a rentable object is already rented.
     * @throws AlreadyDepositException            If a rentable object is already deposited.
     */
    public static void main(String[] args)
            throws InvalidAccomodationCapacityException, FullStationException, EmptySpaceException,
            OutOfServiceException, AlreadyRentedException, AlreadyDepositException {

        ControlCenter controlCenter = ControlCenter.getControlCenter();
        Random r = new Random();

        // Create stations and add them to the control center
        for (int i = 0; i < 15; i++) {
            int rande = r.nextInt(11) + 10;
            controlCenter.addStation(new Station(rande));
        }

        // Create different types of bikes and distribute them to stations
        Bike c = new ClassicBike();
        Bike e = new ElectricBike();

        for (Station s : controlCenter.getStationList()) {
            for (int i = 0; i < s.getAccomodationCapacity(); i++) {
                Boolean bool = r.nextBoolean();
                if (bool) {
                    s.addRentable(new Rentable<Bike>(c), i);
                } else {
                    s.addRentable(new Rentable<Bike>(e), i);
                }
            }
        }

        // Add all rentable objects to the control center's list
        for (Station s : controlCenter.getStationList()) {
            for (Rentable<?> rentable : s.getRentablesList()) {
                if (rentable != null) {
                    controlCenter.getAllRentables().add(rentable);
                }
            }
        }

        // Start the simulation
        controlCenter.simulate();
    }
}
