package Station;

import Exceptions.FullStationException;
import Exceptions.InvalidAccomodationCapacityException;
import Exceptions.OutOfServiceException;

import java.util.ArrayList;
import java.util.List;

import Exceptions.EmptySpaceException;
import Rentable.Rentable;
import equipements.Status;

/**
 * Represents a station for renting and depositing rentable items.
 */
public class Station {
    private int stationId;
    private int accomodationCapacity;
    private Rentable<?>[] rentablesList;
    private int rentedRound;
    private int nbOfDeposit;

    private static int nextStationId = 1;

    /**
     * Constructs a Station with the specified accommodation capacity.
     *
     * @param accomodationCapacity The accommodation capacity of the station.
     * @throws InvalidAccomodationCapacityException If the accommodation capacity is not between 10 and 20.
     */
    public Station(int accomodationCapacity) throws InvalidAccomodationCapacityException {
        this.stationId = nextStationId;
        if (accomodationCapacity < 10 || accomodationCapacity > 20) {
            throw new InvalidAccomodationCapacityException("The accommodation capacity must be between 10 and 20.");
        }
        this.rentedRound = 0;
        this.nbOfDeposit = 0;
        this.accomodationCapacity = accomodationCapacity;
        this.rentablesList = new Rentable<?>[this.accomodationCapacity];
        nextStationId++;
    }

    /**
     * Gets the number of rounds the station has been rented.
     *
     * @return The number of rounds rented.
     */
    public int getRentedRound() {
        return this.rentedRound;
    }

    /**
     * Increments the number of rounds the station has been rented.
     */
    public void incRentedRound() {
        this.rentedRound++;
    }

    /**
     * Resets the number of rounds the station has been rented.
     */
    public void renRentedRound() {
        this.rentedRound = 0;
    }

    /**
     * Gets the number of deposits made at the station.
     *
     * @return The number of deposits.
     */
    public int getNbOfDeposit() {
        return this.nbOfDeposit;
    }

    /**
     * Increments the number of deposits made at the station.
     */
    public void incNbOfDeposit() {
        this.nbOfDeposit++;
    }

    /**
     * Resets the number of deposits made at the station.
     */
    public void renNbOfDeposit() {
        this.nbOfDeposit = 0;
    }

    /**
     * Gets the ID of the station.
     *
     * @return The station ID.
     */
    public int getStationId() {
        return this.stationId;
    }

    /**
     * Gets the accommodation capacity of the station.
     *
     * @return The accommodation capacity.
     */
    public int getAccomodationCapacity() {
        return this.accomodationCapacity;
    }

    /**
     * Gets the array of rentable items at the station.
     *
     * @return The array of rentable items.
     */
    public Rentable<?>[] getRentablesList() {
        return this.rentablesList;
    }

    /**
     * Gets a specific rentable item at a given location.
     *
     * @param emplacement The location of the rentable item.
     * @return The rentable item at the specified location.
     * @throws EmptySpaceException If the space is empty.
     */
    public Rentable<?> getRentable(int emplacement) throws EmptySpaceException {
        if (isEmptySpace(emplacement)) {
            throw new EmptySpaceException("The space is Empty.");
        }
        return this.rentablesList[emplacement];
    }

    /**
     * Gets a list of rentable items excluding those that are out of service.
     *
     * @return The list of rentable items in service.
     */
    public List<Rentable<?>> getJustRentables() {
        List<Rentable<?>> justRentable = new ArrayList<>();
        for (Rentable<?> r : this.rentablesList) {
            if (r != null && r.getStatus() != Status.OUT_OF_SERVICE) {
                justRentable.add(r);
            }
        }
        return justRentable;
    }

    /**
     * Gets a list of IDs of empty spaces at the station.
     *
     * @return The list of IDs of empty spaces.
     */
    public List<Integer> getIdOfEmptyplaces() {
        int res = 0;
        List<Integer> listOfId = new ArrayList<>();
        for (Rentable<?> r : this.rentablesList) {
            if (r == null) {
                listOfId.add(res);
            }
            res++;
        }
        return listOfId;
    }

    /**
     * Gets a list of IDs of non-empty spaces at the station.
     *
     * @return The list of IDs of non-empty spaces.
     */
    public List<Integer> getIdOfNoneEmptyplaces() {
        int res = 0;
        List<Integer> listOfId = new ArrayList<>();
        for (Rentable<?> r : this.rentablesList) {
            if (r != null && r.getStatus() != Status.OUT_OF_SERVICE) {
                listOfId.add(res);
            }
            res++;
        }
        return listOfId;
    }

    /**
     * Adds rentable items from the given list to empty spaces at the station.
     *
     * @param listRentable The list of rentable items to add.
     * @param diff         The number of rentable items to add.
     * @return The remaining list of rentable items.
     */
    public List<Rentable<?>> addRentablesFrom(List<Rentable<?>> listRentable, int diff) {
        for (int i = 0; i < diff; i++) {
            if (listRentable.size() > 0 && this.getIdOfEmptyplaces().size() > 0) {
                Rentable<?> rentable = listRentable.get(0);
                listRentable.remove(rentable);
                this.addRentable(rentable, this.getIdOfEmptyplaces().get(0));
            } else {
                break;
            }
        }
        return listRentable;
    }

    /**
     * Removes a random number of rentable items from the station.
     *
     * @param rand The number of rentable items to remove.
     * @return The list of removed rentable items.
     */
    public List<Rentable<?>> removeRandomRentables(int rand) {
        List<Rentable<?>> removed = new ArrayList<>();
        int i = 0;
        while (rand != 0) {
            if (this.rentablesList[i] != null) {
                removed.add(this.rentablesList[i]);
                this.rentablesList[i] = null;
                rand--;
            }
            i++;
        }
        return removed;
    }

    /**
     * Sets the ID of the station.
     *
     * @param id The new station ID.
     */
    public void setStationId(int id) {
        this.stationId = id;
    }

    /**
     * Sets the accommodation capacity of the station.
     *
     * @param accomodationCapacity The new accommodation capacity.
     */
    public void setAccomodationCapacity(int accomodationCapacity) {
        this.accomodationCapacity = accomodationCapacity;
    }

    /**
     * Sets the array of rentable items at the station.
     *
     * @param rentablesList The new array of rentable items.
     */
    public void setRentablesList(Rentable<?>[] rentablesList) {
        this.rentablesList = rentablesList;
    }

    /**
     * Adds a rentable item to a specific location at the station.
     *
     * @param rentable The rentable item to add.
     * @param i        The location to add the rentable item.
     */
    public void addRentable(Rentable<?> rentable, int i) {
        this.rentablesList[i] = rentable;
    }

    /**
     * Removes a rentable item from a specific location at the station.
     *
     * @param i The location to remove the rentable item.
     */
    public void removeRantble(int i) {
        this.rentablesList[i] = null;
    }

    /**
     * Deposits a rentable item at a specific location in the station.
     *
     * @param rentable     The rentable item to deposit.
     * @param emplacement  The location to deposit the rentable item.
     * @throws FullStationException    If the station is full.
     * @throws EmptySpaceException      If the space is empty.
     */
    public void deposit(Rentable<?> rentable, int emplacement) throws FullStationException, EmptySpaceException {
        if (isFull()) {
            throw new FullStationException("The station is Full.");
        }
        if (!isEmptySpace(emplacement)) {
            throw new EmptySpaceException("The space is not Empty");
        }
        this.rentablesList[emplacement] = rentable;
        rentable.incrementNbUse();
    }

    /**
     * Checks if the station is full.
     *
     * @return true if the station is full, false otherwise.
     */
    public Boolean isFull() {
        for (int i = 0; i < this.rentablesList.length; i++) {
            if (this.rentablesList[i] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a specific space in the station is empty.
     *
     * @param space The location to check.
     * @return true if the space is empty, false otherwise.
     */
    public Boolean isEmptySpace(int space) {
        return this.rentablesList[space] == null;
    }

    /**
     * Checks if the station is empty.
     *
     * @return true if the station is empty, false otherwise.
     */
    public Boolean isEmpty() {
        for (int i = 0; i < this.rentablesList.length; i++) {
            if (this.rentablesList[i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rents a rentable item from a specific location in the station.
     *
     * @param emplacement  The location to rent the rentable item.
     * @return The rented rentable item.
     * @throws EmptySpaceException     If the space is empty.
     * @throws OutOfServiceException   If the rentable item is out of service.
     */
    public Rentable<?> rent(int emplacement) throws EmptySpaceException, OutOfServiceException {
        if (isEmptySpace(emplacement)) {
            throw new EmptySpaceException("The space is Empty.");
        }

        Rentable<?> r = this.rentablesList[emplacement];
        if (r.getStatus() == Status.OUT_OF_SERVICE) {
            throw new OutOfServiceException("The rentable is Out of Service.");
        }
        this.rentablesList[emplacement] = null;
        return r;
    }

    /**
     * Checks if this station is equal to another object.
     *
     * @param o The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (!(o instanceof Station))
            return false;
        Station other = (Station) o;
        return (this.stationId == other.getStationId());
    }
}
