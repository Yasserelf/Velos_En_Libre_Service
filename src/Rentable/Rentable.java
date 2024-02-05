package Rentable;

import equipements.Status;

/**
 * A generic class representing a rentable item.
 * 
 * @param <T> The type of the rentable item that extends CanBeRented.
 */
public class Rentable<T extends CanBeRented> {
    private int rentableId;
    private Status status;
    private int currentNbUse;
    private T theRentable;
    private final static int MAX_OF_RENT = 5;

    private static int nextRentableId = 1;

    /**
     * Constructs a Rentable instance.
     *
     * @param theRentable The rentable item to be associated with this Rentable
     *                    instance.
     */
    public Rentable(T theRentable) {
        this.rentableId = nextRentableId;
        this.status = Status.IN_SERVICE;
        this.currentNbUse = 0;
        this.theRentable = theRentable;
        nextRentableId++;
    }

    /**
     * Gets the ID of the Rentable item.
     *
     * @return The ID of the Rentable item.
     */
    public int getId() {
        return this.rentableId;
    }

    /**
     * Gets the status of the Rentable item.
     *
     * @return The status of the Rentable item.
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * Gets the current number of uses of the Rentable item.
     *
     * @return The current number of uses.
     */
    public int getCurrentNbUse() {
        return this.currentNbUse;
    }

    /**
     * Gets the associated rentable item.
     *
     * @return The rentable item.
     */
    public T getTheRentable() {
        return this.theRentable;
    }

    /**
     * Sets the ID of the Rentable item.
     *
     * @param id The new ID.
     */
    public void setRentableId(int id) {
        this.rentableId = id;
    }

    /**
     * Sets the status of the Rentable item.
     *
     * @param status The new status.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Sets the current number of uses of the Rentable item.
     *
     * @param n The new number of uses.
     */
    public void setCurrentNbUse(int n) {
        this.currentNbUse = n;
    }

    /**
     * Checks if the Rentable item is out of service based on the maximum number of
     * uses.
     */
    public void isOutOfService() {
        if (MAX_OF_RENT <= this.getCurrentNbUse()) {
            this.markAsOutOfService();
        }
    }

    /**
     * Marks the Rentable item as out of service.
     */
    public void markAsOutOfService() {
        this.status = Status.OUT_OF_SERVICE;
    }

    public Integer getMaxOfRent(){
        return Rentable.MAX_OF_RENT;
    } 

    /**
     * Increments the current number of uses and checks if the Rentable item is out
     * of service.
     */
    public void incrementNbUse() {
        this.currentNbUse++;
        this.isOutOfService();
    }

    /**
     * Marks the Rentable item as stolen.
     */
    public void markAsStolen() {
        this.status = Status.STOLEN;
    }

    /**
     * Checks if this Rentable item is equal to another object.
     *
     * @param o The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (!(o instanceof Rentable<?>))
            return false;
        Rentable<?> other = (Rentable<?>) o;
        return (this.rentableId == other.getId());
    }
}
