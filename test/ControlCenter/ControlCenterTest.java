package ControlCenter;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import Exceptions.AlreadyDepositException;
import Exceptions.AlreadyRentedException;
import Exceptions.EmptySpaceException;
import Exceptions.FullStationException;
import Exceptions.InvalidAccomodationCapacityException;
import Exceptions.OutOfServiceException;
import Rentable.ClassicBike;
import Rentable.ElectricBike;
import Rentable.Rentable;
import Staff.Repairer;
import Station.Station;
import Strategie.EqualRedistribution;
import Strategie.RedistributionStrategy;
import ControlCenter.MockEqualRedistribution;

public class ControlCenterTest {

    private ControlCenter controlCenter = ControlCenter.getControlCenter();

    @Test
    public void testAddStation() throws InvalidAccomodationCapacityException {

        Station station = new Station(10);

        this.controlCenter.addStation(station);

        assertTrue(controlCenter.getStationList().contains(station));
    }

    @Test
    public void testPutEmptyOrFullStations() throws InvalidAccomodationCapacityException, FullStationException,
            EmptySpaceException, OutOfServiceException {
        Station station1 = new Station(10);
        Station station2 = new Station(11);
        Station station3 = new Station(15);
        this.controlCenter.addStation(station1);
        this.controlCenter.addStation(station2);
        this.controlCenter.addStation(station3);

        assertTrue(this.controlCenter.getEmptyOrFullStations().isEmpty());

        station2.deposit(new Rentable<>(new ElectricBike()), 0);
        this.controlCenter.putEmptyOrFullStations();

        for (int i = 0; i < station3.getAccomodationCapacity(); i++) {
            station3.deposit(new Rentable<>(new ClassicBike()), i);
        }

        assertTrue(this.controlCenter.getEmptyOrFullStations().containsKey(station1));
        assertTrue(this.controlCenter.getEmptyOrFullStations().containsKey(station3));
        assertFalse(this.controlCenter.getEmptyOrFullStations().containsKey(station2));

        station1.deposit(new Rentable<>(new ElectricBike()), 0);
        station2.rent(0);
        station3.rent(5);
        this.controlCenter.putEmptyOrFullStations();

        assertFalse(this.controlCenter.getEmptyOrFullStations().containsKey(station1));
        assertTrue(this.controlCenter.getEmptyOrFullStations().containsKey(station2));
        assertFalse(this.controlCenter.getEmptyOrFullStations().containsKey(station3));
    }

    @Test
    public void testPutIfCanBeStolen()
            throws InvalidAccomodationCapacityException, FullStationException, EmptySpaceException,
            AlreadyDepositException, AlreadyRentedException, OutOfServiceException {
        Rentable<?> r = new Rentable<>(new ElectricBike());
        Station s = new Station(10);
        this.controlCenter.addStation(s);
        this.controlCenter.setTime(0);
        assertTrue(this.controlCenter.getCanBeStolen().isEmpty());
        this.controlCenter.beforeDeposit(r, s, 2);
        this.controlCenter.putIfCanBeStolen();
        assertFalse(this.controlCenter.getCanBeStolen().isEmpty());
        assertEquals(this.controlCenter.getCanBeStolen().get(r)[0], 2 * this.controlCenter.getRedistributionDuration());
        assertEquals(s.getStationId(), this.controlCenter.getCanBeStolen().get(r)[1]);
        assertEquals(this.controlCenter.getCanBeStolen().get(r)[2], 2);

        this.controlCenter.beforeRent(s, 2);
        this.controlCenter.putIfCanBeStolen();
        assertTrue(this.controlCenter.getCanBeStolen().isEmpty());
    }

    @Test
    public void testCheckIfRedistribute() throws InvalidAccomodationCapacityException, EmptySpaceException,
            OutOfServiceException, FullStationException {
        Station station1 = new Station(10);
        MockEqualRedistribution mockEqualRedistribution = new MockEqualRedistribution();
        this.controlCenter.addStation(station1);
        this.controlCenter.setRedistributionStrategy(mockEqualRedistribution);
        this.controlCenter.setTime(0);
        this.controlCenter.putEmptyOrFullStations();
        assertEquals(2 * this.controlCenter.getRedistributionDuration(),
                this.controlCenter.getEmptyOrFullStations().get(station1));
        this.controlCenter.setTime(2 * this.controlCenter.getRedistributionDuration());
        this.controlCenter.checkIfRedistribute();
        assertEquals(mockEqualRedistribution.getcheckIfRedistribute(), 1);
    }

    @Test
    public void testBeforeDepositObjectAlreadyDepositedInStation()
            throws AlreadyDepositException, FullStationException, EmptySpaceException,
            InvalidAccomodationCapacityException {
        Station station1 = new Station(10);
        Station station2 = new Station(10);
        Rentable<?> rentable = new Rentable<>(new ElectricBike());

        this.controlCenter.addStation(station1);
        this.controlCenter.addStation(station2);
        station2.deposit(rentable, 0);

        assertThrows(AlreadyDepositException.class, () -> this.controlCenter.beforeDeposit(rentable, station1, 1));
    }

    @Test
    public void testBeforeDepositSuccessfulDeposit()
            throws AlreadyDepositException, FullStationException, EmptySpaceException,
            InvalidAccomodationCapacityException {
        Station station = new Station(10);
        Rentable<?> rentable = new Rentable<>(new ElectricBike());
        this.controlCenter.addStation(station);

        assertTrue(station.isEmptySpace(1));
        this.controlCenter.beforeDeposit(rentable, station, 1);
        assertFalse(station.isEmptySpace(1));
    }

    @Test
    public void testBeforeRentObjectNotFound()
            throws AlreadyRentedException, EmptySpaceException, OutOfServiceException,
            InvalidAccomodationCapacityException {
        Station station = new Station(10);
        this.controlCenter.addStation(station);

        assertThrows(EmptySpaceException.class, () -> this.controlCenter.beforeRent(station, 1));
    }

    @Test
    public void testBeforeRentObjectAlreadyRented()
            throws AlreadyRentedException, EmptySpaceException, OutOfServiceException,
            InvalidAccomodationCapacityException, FullStationException {
        Station station = new Station(10);
        Rentable<?> rentable = new Rentable<>(new ElectricBike());
        this.controlCenter.addStation(station);
        station.deposit(rentable, 1);
        this.controlCenter.getRentedList().add(rentable);

        assertThrows(AlreadyRentedException.class, () -> this.controlCenter.beforeRent(station, 1));
    }

    @Test
    public void testBeforeRentSuccessfulRent()
            throws AlreadyRentedException, EmptySpaceException, OutOfServiceException,
            InvalidAccomodationCapacityException, FullStationException {
        Station station = new Station(10);
        Rentable<?> rentable = new Rentable<>(new ElectricBike());
        this.controlCenter.addStation(station);
        station.deposit(rentable, 1);

        assertTrue(this.controlCenter.getRentedList().isEmpty());
        this.controlCenter.beforeRent(station, 1);
        assertFalse(this.controlCenter.getRentedList().isEmpty());
        assertEquals(1, this.controlCenter.getRentedList().size());
    }

    @Test
    public void testSendToRepair() {
        Repairer repairer = this.controlCenter.getRepairer();

        Rentable<?> rentable = new Rentable<>(new ElectricBike());
        this.controlCenter.sendToRepair(rentable);

        assertTrue(repairer.getOnRepair().containsKey(rentable));

        int expectedRepairTime = this.controlCenter.checkTime() + this.controlCenter.getReparationDuration();
        assertEquals(expectedRepairTime, (int) repairer.getOnRepair().get(rentable));

    }

}
