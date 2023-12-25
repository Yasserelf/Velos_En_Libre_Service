package Station;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import Rentable.*;
import equipements.*;
import Exceptions.*;

public class StationTest {

	private Station station;
	private Bike electricBike;
	private Bike classicBike;
	private Rentable<Bike> rentable;
	private Rentable<Bike> rentable1;

	@BeforeEach
	public void init() throws InvalidAccomodationCapacityException, FullStationException, EmptySpaceException {
		this.station = new Station(12);
		this.electricBike = new ElectricBike();
		this.classicBike = new ClassicBike();
		this.rentable = new Rentable<>(this.classicBike);
		this.rentable1 = new Rentable<>(this.electricBike);

	}

	@Test
	public void isFullTest() throws FullStationException, EmptySpaceException {
		assertFalse(this.station.isFull());
		for (int i = 0; i < this.station.getRentablesList().length; i++) {
			this.station.deposit(this.rentable, i);
		}
		assertTrue(this.station.isFull());
	}

	@Test
	public void isEmptySpaceTest() throws FullStationException, EmptySpaceException {
		assertTrue(this.station.isEmptySpace(2));
		assertTrue(this.station.isEmptySpace(10));
		this.station.deposit(this.rentable1, 2);
		this.station.deposit(this.rentable, 10);
		assertFalse(this.station.isEmptySpace(2));
		assertFalse(this.station.isEmptySpace(10));
	}

	@Test
	public void isEmptyTest() throws FullStationException, EmptySpaceException {
		assertTrue(this.station.isEmpty());
		this.station.deposit(this.rentable, 11);
		assertFalse(this.station.isEmpty());
	}

	@Test
	public void depositWhenEverythingOkTest() throws FullStationException, EmptySpaceException {
		assertTrue(this.station.isEmpty());
		assertFalse(this.station.isFull());
		this.station.deposit(this.rentable1, 0);
		assertFalse(this.station.isEmptySpace(0));
		assertEquals(this.station.getRentablesList()[0], this.rentable1);
	}

	@Test
	public void depositWhenStationIsFullTest() {
		for (int i = 0; i < this.station.getAccomodationCapacity(); i++) {
			try {
				this.station.deposit(this.rentable1, i);
			} catch (FullStationException | EmptySpaceException e) {
				fail("Should not throw an exception here.");
			}
		}
		assertThrows(FullStationException.class, () -> this.station.deposit(this.rentable1, 0));
	}

	@Test
	public void depositWhenSpaceIsNotEmptyTest() {
		try {
			this.station.deposit(this.rentable, 0);
		} catch (FullStationException | EmptySpaceException e) {
			fail("Should not throw an exception here.");
		}
		assertThrows(EmptySpaceException.class, () -> this.station.deposit(this.rentable1, 0));
	}

	@Test
	public void testRentWhenSpaceIsEmpty() {
		assertTrue(this.station.isEmptySpace(0));
		assertThrows(EmptySpaceException.class, () -> this.station.rent(0));
	}

	@Test
	public void testRentWhenSpaceIsNotEmpty() throws EmptySpaceException, FullStationException, OutOfServiceException {
		assertTrue(this.station.isEmptySpace(0));
		this.station.deposit(this.rentable, 0);
		assertFalse(this.station.isEmptySpace(0));
		this.station.rent(0);
		assertTrue(this.station.isEmptySpace(0));
	}

	@Test
	public void testRentWhenSpaceIsOutOfService() throws FullStationException, EmptySpaceException {

		this.station.deposit(this.rentable, 0);
		this.rentable.markAsOutOfService();
		assertThrows(OutOfServiceException.class, () -> this.station.rent(0));
	}

	/**
	 * @throws FullStationException
	 * @throws EmptySpaceException
	 */
	@Test
	public void testGetIdOfEmptyplaces() throws FullStationException, EmptySpaceException {

		this.station.deposit(this.rentable, 0);
		this.station.deposit(this.rentable1, 2);

		List<Integer> emptyPlaces = this.station.getIdOfEmptyplaces();

		assertTrue(emptyPlaces.contains(1));
		assertTrue(emptyPlaces.contains(3));
		assertFalse(emptyPlaces.contains(0));

	}

	@Test
	public void testGetIdOfNoneEmptyplaces() throws FullStationException, EmptySpaceException {

		this.station.deposit(this.rentable, 0);
		this.station.deposit(this.rentable1, 1);

		Rentable<?> outOfServiceBike = new Rentable<>(new ClassicBike());
		outOfServiceBike.markAsOutOfService();
		this.station.deposit(outOfServiceBike, 2);

		List<Integer> noneEmptyPlaces = this.station.getIdOfNoneEmptyplaces();

		assertTrue(noneEmptyPlaces.contains(1));
		assertTrue(noneEmptyPlaces.contains(0));
		assertFalse(noneEmptyPlaces.contains(2));
		assertFalse(noneEmptyPlaces.contains(4));

	}

	@Test
	public void testGetJustRentables() throws FullStationException, EmptySpaceException {

		this.station.deposit(this.rentable, 0);
		this.station.deposit(this.rentable1, 1);
		this.station.deposit(new Rentable<>(new ElectricBike()), 2);

		Rentable<?> outOfServiceBike = new Rentable<>(new ClassicBike());
		outOfServiceBike.markAsOutOfService();
		this.station.deposit(outOfServiceBike, 3);

		List<Rentable<?>> justRentables = this.station.getJustRentables();

		for (Rentable<?> rentable : justRentables) {
			assertNotEquals(Status.OUT_OF_SERVICE, rentable.getStatus());
		}

		assertEquals(3, justRentables.size());
		assertFalse(justRentables.contains(outOfServiceBike));
	}

	@Test
	public void testRemoveRandomRentables() throws FullStationException, EmptySpaceException {

		this.station.deposit(new Rentable<>(new ElectricBike()), 0);
		this.station.deposit(new Rentable<>(new ClassicBike()), 1);
		this.station.deposit(new Rentable<>(new ElectricBike()), 2);

		List<Rentable<?>> removedRentables = this.station.removeRandomRentables(2);

		assertEquals(2, removedRentables.size());

		assertTrue(this.station.isEmptySpace(0));
		assertTrue(this.station.isEmptySpace(1));
		assertFalse(this.station.isEmptySpace(2));
	}

	@Test
	public void testAddRentablesFrom() throws FullStationException, EmptySpaceException {

		List<Rentable<?>> listRentable = new ArrayList<>();
		listRentable.add(this.rentable);
		listRentable.add(this.rentable1);

		this.station.addRentablesFrom(listRentable, 2);

		assertFalse(this.station.isEmptySpace(0));
		assertFalse(this.station.isEmptySpace(1));
		assertTrue(this.station.isEmptySpace(2));
		assertTrue(listRentable.isEmpty());
	}

}
