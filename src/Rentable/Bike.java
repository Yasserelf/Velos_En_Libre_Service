package Rentable;

import java.util.*;

import equipements.Equipment;

/**
 * Abstract class representing a rentable bike.
 * This class extends the abstract class CanBeRented.
 */
public abstract class Bike extends CanBeRented {

    /** List of equipment associated with the bike. */
    protected List<Equipment> equipments;

    /**
     * Default constructor for the Bike class.
     * Initializes the equipment list with a new ArrayList.
     */
    public Bike() {
        this.equipments = new ArrayList<>();
    }

    /**
     * Checks if the bike is equipped with a specific equipment.
     *
     * @param e The equipment to check.
     * @return true if the bike is equipped with the specified equipment, otherwise false.
     */
    public boolean isEquipped(Equipment e) {
        return this.equipments.contains(e);
    }

    /**
     * Adds an equipment to the bike.
     *
     * @param obj The equipment to add.
     */
    public void addEquipment(Equipment obj) {
        this.equipments.add(obj);
    }

    /**
     * Gets the list of equipment associated with the bike.
     *
     * @return The list of bike's equipment.
     */
    public List<Equipment> getEquipmentList() {
        return this.equipments;
    }

    /**
     * Sets the list of equipment associated with the bike.
     *
     * @param l The new list of equipment.
     */
    public void setEquipmentList(List<Equipment> l) {
        this.equipments = l;
    }

}
