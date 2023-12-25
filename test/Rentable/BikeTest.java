package Rentable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import equipements.Equipment;

public abstract class BikeTest {

	protected Bike bike;

	protected abstract Bike createBike();

	@BeforeEach
	public void init() {
		this.bike = this.createBike();
	}

	@Test
	public void isEquipedTest() {
		assertFalse(this.bike.isEquiped(Equipment.BASKET));
		assertFalse(this.bike.isEquiped(Equipment.LUGGAGE_RACK));
		this.bike.addEquipment(Equipment.BASKET);
		assertTrue(this.bike.isEquiped(Equipment.BASKET));
		assertFalse(this.bike.isEquiped(Equipment.LUGGAGE_RACK));
	}

	@Test
	public void addEquipmentTest() {
		assertEquals(0, this.bike.getEquipmentList().size());
		this.bike.addEquipment(Equipment.BASKET);
		assertEquals(1, this.bike.getEquipmentList().size());
		this.bike.addEquipment(Equipment.LUGGAGE_RACK);
		assertEquals(2, this.bike.getEquipmentList().size());
		assertTrue(this.bike.getEquipmentList().contains(Equipment.BASKET));
	}

}
