package Station;

import Exceptions.FullStationException;
import Exceptions.InvalidAccomodationCapacityException;
import Exceptions.OutOfServiceException;

import java.util.ArrayList;
import java.util.List;

import Exceptions.EmptySpaceException;
import Rentable.Rentable;
import equipements.Status;

public class Station {
	private int stationId;
	private int accomodationCapacity;
	private Rentable<?>[] rentablesList;
	private int rentedRound;
	private int nbOfDeposit;

	private static int nextStationId = 1;

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

	public int getRendtedRound() {
		return this.rentedRound;
	}

	public void incRentedRound() {
		this.rentedRound++;
	}

	public void renRentedRound() {
		this.rentedRound = 0;
	}

	public int getNbOfDeposit() {
		return this.nbOfDeposit;
	}

	public void incNbOfDeposit() {
		this.nbOfDeposit++;
	}

	public void renNbOfDeposit() {
		this.nbOfDeposit = 0;
	}

	public int getStationId() {
		return this.stationId;
	}

	public int getAccomodationCapacity() {
		return this.accomodationCapacity;
	}

	public Rentable<?>[] getRentablesList() {
		return this.rentablesList;
	}

	public Rentable<?> getRentable(int emplacement) throws EmptySpaceException {
		if (isEmptySpace(emplacement)) {
			throw new EmptySpaceException("The space is Empty.");
		}
		return this.rentablesList[emplacement];
	}

	public List<Rentable<?>> getJustRentables() {
		List<Rentable<?>> justRentable = new ArrayList<>();
		for (Rentable<?> r : this.rentablesList) {
			if (r != null && r.getStatus() != Status.OUT_OF_SERVICE) {
				justRentable.add(r);
			}
		}
		return justRentable;
	}

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

	public void setStationId(int id) {
		this.stationId = id;
	}

	public void setAccomodationCapacity(int accomodationCapacity) {
		this.accomodationCapacity = accomodationCapacity;
	}

	public void setRentablesList(Rentable<?>[] rentablesList) {
		this.rentablesList = rentablesList;
	}

	public void addRentable(Rentable<?> rentable, int i) {
		this.rentablesList[i] = rentable;
	}

	public void removeRantble(int i) {
		this.rentablesList[i] = null;
	}

	/**
	 * @param rentable
	 * @param emplacement
	 * @throws FullStationException
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

	public Boolean isFull() {
		for (int i = 0; i < this.rentablesList.length; i++) {
			if (this.rentablesList[i] == null) {
				return false;
			}
		}
		return true;
	}

	public Boolean isEmptySpace(int space) {
		return this.rentablesList[space] == null;
	}

	public Boolean isEmpty() {
		for (int i = 0; i < this.rentablesList.length; i++) {
			if (this.rentablesList[i] != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param emplacement
	 * @return
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

	public boolean equals(Object o) {
		if (!(o instanceof Station))
			return false;
		Station other = (Station) o;
		return (this.stationId == other.getStationId());
	}

}
