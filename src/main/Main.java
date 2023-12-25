package main;

import java.util.*;

import ControlCenter.*;
import Exceptions.AlreadyDepositException;
import Exceptions.AlreadyRentedException;
import Exceptions.EmptySpaceException;
import Exceptions.FullStationException;
import Exceptions.InvalidAccomodationCapacityException;
import Exceptions.OutOfServiceException;
import Station.Station;
import Rentable.Bike;
import Rentable.ClassicBike;
import Rentable.ElectricBike;
import Rentable.Rentable;

public class Main {

    /**
     * @param <T>
     * @param args
     * @throws InvalidAccomodationCapacityException
     * @throws FullStationException
     * @throws EmptySpaceException
     * @throws OutOfServiceException
     * @throws AlreadyRentedException
     * @throws AlreadyDepositException
     */
    public static <T> void main(String[] args)
            throws InvalidAccomodationCapacityException, FullStationException, EmptySpaceException,
            OutOfServiceException, AlreadyRentedException, AlreadyDepositException {

        ControlCenter controlCenter = ControlCenter.getControlCenter();
        Random r = new Random();
        for (int i = 0; i < 15; i++) {
            int rande = r.nextInt(10, 21);
            controlCenter.addStation(new Station(rande));
        }
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

        for (Station s : controlCenter.getStationList()) {
            for (Rentable<?> rentable : s.getRentablesList()) {
                if (rentable != null) {
                    controlCenter.getAllRentables().add(rentable);
                }

            }
        }

        controlCenter.simulate();

    }
}
