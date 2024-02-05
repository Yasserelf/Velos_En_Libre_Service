package ControlCenter;

import Rentable.*;
import Station.Station;
import Strategie.EqualRedistribution;
import Strategie.RedistributionStrategy;
import Staff.Repairer;


import java.util.*;
import equipements.Status;
import Exceptions.AlreadyDepositException;
import Exceptions.AlreadyRentedException;
import Exceptions.EmptySpaceException;
import Exceptions.FullStationException;
import Exceptions.OutOfServiceException;

/**
 * The central control center managing the bike rental system.
 */
public class ControlCenter {
    private List<Rentable<?>> allRentables;
    private List<Rentable<?>> rentedList;
    private static int redistributionDuration = 5;
    private List<Station> stationList;
    private int timeNow;
    private static int repairDuration = 5;
    private static ControlCenter controlCenter;
    private Repairer repairer;
    private Random random = new Random();
    private Map<Station, Integer> emptyOrFullStations;
    private Map<Rentable<?>, Integer[]> canBeStolen;
    private Integer red = 0;
    private int nbOfRentedTotal;
    private int nbOfStolen;
    private int nbOfTotalDeposit;
    private int nbOfTotalRepairRound;
    private RedistributionStrategy redistributionStrategy;

    /**
     * Private constructor to enforce the singleton pattern.
     */
    private ControlCenter() {
        this.allRentables = new ArrayList<Rentable<?>>();
        this.rentedList = new ArrayList<Rentable<?>>();
        this.stationList = new ArrayList<Station>();
        this.timeNow = 0;
        this.repairer = new Repairer();
        this.redistributionStrategy = new EqualRedistribution();
        this.emptyOrFullStations = new HashMap<>();
        this.canBeStolen = new HashMap<>();
        this.nbOfRentedTotal = 0;
        this.nbOfStolen = 0;
        this.nbOfTotalDeposit = 0;
        this.nbOfTotalRepairRound = 0;
    }

    /**
     * Gets the singleton instance of the control center.
     *
     * @return The control center instance.
     */
    public static ControlCenter getControlCenter() {
        if (controlCenter == null) {
            controlCenter = new ControlCenter();
        }
        return controlCenter;
    }

    public void setNullControlCenter() {
        controlCenter = null;
    }

    /**
     * Gets the list of all rentable items.
     *
     * @return The list of all rentable items.
     */
    public List<Rentable<?>> getAllRentables() {
        return this.allRentables;
    }

    /**
     * Gets the duration for redistribution.
     *
     * @return The redistribution duration.
     */
    public int getRedistributionDuration() {
        return ControlCenter.redistributionDuration;
    }

    /**
     * Sets the redistribution strategy.
     *
     * @param strategy The redistribution strategy to set.
     */
    public void setRedistributionStrategy(RedistributionStrategy strategy) {
        this.redistributionStrategy = strategy;
    }

    /**
     * Gets the list of stations.
     *
     * @return The list of stations.
     */
    public List<Station> getStationList() {
        return this.stationList;
    }

    /**
     * Gets the list of rented items.
     *
     * @return The list of rented items.
     */
    public List<Rentable<?>> getRentedList() {
        return this.rentedList;
    }

    /**
     * Sets the list of all rentable items.
     *
     * @param allRentables The list of all rentable items to set.
     */
    public void setAllRentables(List<Rentable<?>> allRentables) {
        this.allRentables = allRentables;
    }

    /**
     * Sets the redistribution duration.
     *
     * @param duration The redistribution duration to set.
     */
    public void setRedistributionDuration(int duration) {
        ControlCenter.redistributionDuration = duration;
    }

    /**
     * Sets the list of stations.
     *
     * @param stations The list of stations to set.
     */
    public void setStationList(List<Station> stations) {
        this.stationList = stations;
    }

    /**
     * Sets the list of rented items.
     *
     * @param rentedList The list of rented items to set.
     */
    public void setRentedList(List<Rentable<?>> rentedList) {
        this.rentedList = rentedList;
    }

    /**
     * Adds a station to the list of stations.
     *
     * @param s The station to add.
     */
    public void addStation(Station s) {
        if (!this.stationList.contains(s))
            this.stationList.add(s);
    }

    /**
     * Checks the current time.
     *
     * @return The current time.
     */
    public int checkTime() {
        return this.timeNow;
    }

    /**
     * Puts stations in the map for empty or full stations.
     */
    public void putEmptyOrFullStations() {
        List<Station> stations = new ArrayList<>();
        for (Station s : this.stationList) {
            if ((s.isEmpty() || s.isFull()) && ((!this.emptyOrFullStations.containsKey(s)))) {
                this.emptyOrFullStations.put(s, this.timeNow + 2 * ControlCenter.redistributionDuration);
            }
        }
        for (Station s : this.emptyOrFullStations.keySet()) {
            if (!(s.isEmpty() || s.isFull())) {
                stations.add(s);
            }
        }
        for (Station s : stations) {
            this.emptyOrFullStations.remove(s);
        }
    }

    /**
     * Checks if redistribution is required and initiates the redistribution.
     *
     * @throws EmptySpaceException   If there is an empty space.
     * @throws OutOfServiceException If the rentable is out of service.
     * @throws FullStationException  If the station is full.
     */
    public void checkIfRedistribute() throws EmptySpaceException, OutOfServiceException, FullStationException {
        for (Station s : this.emptyOrFullStations.keySet()) {
            if (this.timeNow == this.emptyOrFullStations.get(s)) {
                System.out.println("-------------------------");
                System.out.println("Redistribution is going to start, here is our current state of stations : ");
                for (Station stat : this.stationList) {
                    System.out.println("Station Name: " + stat.getStationId()
                            + " Number of available for rent Rentables is : " + stat.getJustRentables().size());
                }
                this.redistributionStrategy.redistribute(this.stationList);
                System.out.println();
                System.out.println("Redistribution started,  here is the state of stations after redistribution : ");
                System.out.println();
                for (Station stat : this.stationList) {
                    System.out.println("Station Name: " + stat.getStationId()
                            + " Number of available for rent Rentables is : " + stat.getJustRentables().size());
                }
                System.out.println("-------------------------");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.red++;
                this.emptyOrFullStations.clear();
                break;
            }
        }
    }

    /**
     * Puts rentables in the map if they can be stolen.
     *
     * @throws EmptySpaceException If there is an empty space.
     */
    public void putIfCanBeStolen() throws EmptySpaceException {
        List<Rentable<?>> removedList = new ArrayList<>();
        for (Station s : this.stationList) {
            for (int re : s.getIdOfNoneEmptyplaces()) {
                Integer[] t = { this.timeNow + 2 * ControlCenter.redistributionDuration, s.getStationId(), re };
                if (!this.canBeStolen.containsKey(s.getRentable(re))) {
                    this.canBeStolen.put(s.getRentable(re), t);
                }
            }
        }
        for (Rentable<?> r : this.canBeStolen.keySet()) {
            Integer[] t = this.canBeStolen.get(r);
            for (Station s : this.stationList) {
                if (s.getStationId() == t[1]) {
                    if (s.getRentablesList()[t[2]] != r) {
                        removedList.add(r);
                    }
                }
            }
        }
        for (Rentable<?> r : removedList) {
            this.canBeStolen.remove(r);
        }
    }

    /**
     * Checks if rentables are stolen and removes them if necessary.
     */
    public void checkIfStolen() {
        Rentable<?> removedRentable = null;
        for (Rentable<?> r : this.canBeStolen.keySet()) {
            Integer[] t = this.canBeStolen.get(r);
            if (this.timeNow == t[0]) {
                for (Station s : this.stationList) {
                    if (s.getStationId() == t[1]) {
                        int rand = this.random.nextInt(100);
                        if (rand == 1) {
                            s.removeRantble(t[2]);
                            removedRentable = r;
                            this.nbOfStolen++;
                            break;
                        }
                    }
                }
            }
        }
        if (removedRentable != null)
            this.canBeStolen.remove(removedRentable);
        ;
    }

    /**
     * Sends a rentable to repair.
     *
     * @param r The rentable to send to repair.
     */
    public void sendToRepair(Rentable<?> r) {
        this.repairer.addOnRepair(r, this.timeNow + ControlCenter.repairDuration);
        this.nbOfTotalRepairRound++;
    }

    /**
     * Processes rentable deposit before actual deposit.
     *
     * @param r           The rentable to deposit.
     * @param station     The station to deposit the rentable.
     * @param emplacement The emplacement to deposit the rentable.
     * @throws AlreadyDepositException If the rentable is already deposited.
     * @throws FullStationException    If the station is full.
     * @throws EmptySpaceException     If there is an empty space.
     */
    public void beforeDeposit(Rentable<?> r, Station station, int emplacement)
            throws AlreadyDepositException, FullStationException, EmptySpaceException {
        for (Station s : this.getStationList()) {
            for (Rentable<?> rentable : s.getRentablesList()) {
                if (r.equals(rentable)) {
                    throw new AlreadyDepositException("This object is already exist.");
                }
            }
        }
        this.rentedList.remove(r);
        station.deposit(r, emplacement);
        this.nbOfTotalDeposit++;
        station.incNbOfDeposit();
        if (r.getStatus() == Status.OUT_OF_SERVICE) {
            this.sendToRepair(r);
        }
    }

    /**
     * Processes rentable rent before actual rent.
     *
     * @param station     The station from which the rentable is rented.
     * @param emplacement The emplacement from which the rentable is rented.
     * @throws AlreadyRentedException If the rentable is already rented.
     * @throws EmptySpaceException    If there is an empty space.
     * @throws OutOfServiceException  If the rentable is out of service.
     */
    public void beforeRent(Station station, int emplacement)
            throws AlreadyRentedException, EmptySpaceException, OutOfServiceException {
        Rentable<?> r = null;
        for (Station s : this.stationList) {
            if (s.getStationId() == station.getStationId()) {
                r = s.getRentablesList()[emplacement];
            }
        }
        if (r == null) {
            throw new EmptySpaceException("there is no station controlled by this");
        }
        if (this.getRentedList().contains(r)) {
            throw new AlreadyRentedException("This object is already rented");
        }
        rentedList.add(r);
        station.rent(emplacement);
        this.nbOfRentedTotal++;
        station.incRentedRound();
    }

    /**
     * Prints statistics related to the control center's state.
     *
     * @param nbRepaired The number of rentables repaired.
     */
    public void printStatistics(int nbRepaired) {
        for (Station s : this.stationList) {
            System.out.println("Station Name: " + s.getStationId() + " Rented from : " + s.getRentedRound()
                    + " Deposit in : " + s.getNbOfDeposit());
            s.renNbOfDeposit();
            s.renRentedRound();
        }
        System.out.println("Time now: " + this.timeNow + " Total Rented: " + this.nbOfRentedTotal + " Total Deposit: "
                + this.nbOfTotalDeposit);
        System.out.println("Total Stolen: " + this.nbOfStolen + " Went to repair this Round: "
                + this.nbOfTotalRepairRound + " Repaired this Round: " + nbRepaired);
        System.out.println("Number Of Redistributions : " + this.red);
        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------------");
        this.nbOfTotalRepairRound = 0;
    }

    /**
     * Simulates the operation of the control center.
     *
     * @throws AlreadyRentedException  If the rentable is already rented.
     * @throws EmptySpaceException     If there is an empty space.
     * @throws OutOfServiceException   If the rentable is out of service.
     * @throws AlreadyDepositException If the rentable is already deposited.
     * @throws FullStationException    If the station is full.
     */
    public void simulate() throws AlreadyRentedException, EmptySpaceException, OutOfServiceException,
            AlreadyDepositException, FullStationException {
        int randomLocation;
        int res;

        while (true) {
            res = random.nextInt(this.stationList.size() / 2) + this.stationList.size() / 2;
            while (res >= 0) {
                int randomIndexOfStation = this.getRandomWeightedStationIndex(1);
                if (this.stationList.get(randomIndexOfStation).getIdOfNoneEmptyplaces().size() > 0) {
                    int randomL = random
                            .nextInt(this.stationList.get(randomIndexOfStation).getIdOfNoneEmptyplaces().size());
                    randomLocation = this.stationList.get(randomIndexOfStation).getIdOfNoneEmptyplaces()
                            .get(randomL);
                    this.beforeRent(this.stationList.get(randomIndexOfStation), randomLocation);
                }
                res--;
            }

            res = random.nextInt(this.rentedList.size() / 2);
            while (res > 0) {
                if (!this.stationList.isEmpty()) {
                    int randomStationIndex = this.getRandomWeightedStationIndex(3);
                    Station randomStation = this.stationList.get(randomStationIndex);
                    if (randomStation.getIdOfEmptyplaces().size() > 0) {
                        int randomL = random.nextInt(randomStation.getIdOfEmptyplaces().size());
                        randomLocation = randomStation.getIdOfEmptyplaces().get(randomL);
                        this.beforeDeposit(this.getRentedList().get(res), randomStation, randomLocation);
                        res--;
                    }
                }
            }

            int repaired = this.repairer.checkWhoSBack(this.timeNow);
            this.putIfCanBeStolen();
            this.checkIfStolen();
            this.putEmptyOrFullStations();
            this.checkIfRedistribute();
            this.printStatistics(repaired);

            this.timeNow++;
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets a random weighted station index.
     *
     * @param choix The choice between rent and deposit.
     * @return The index of the randomly selected station.
     */
    private int getRandomWeightedStationIndex(int choix) {
        List<Double> weightsList;
        List<Double> weightsListdep = new ArrayList<>();
        List<Double> weightsListrent = new ArrayList<>();
        int BigStations = this.stationList.size() / 5;
        int j = 0;
        for (Station s : this.stationList) {
            if (j < BigStations) {
                weightsListrent.add(2 * random.nextDouble() + 2);
                weightsListdep.add(2 * random.nextDouble());
            } else {
                weightsListrent.add(2 * random.nextDouble());
                weightsListdep.add(2 * random.nextDouble() + 2);
            }
            j++;
        }
        if (choix == 1) {
            weightsList = weightsListrent;

        } else {
            weightsList = weightsListdep;
        }

        double totalWeight = weightsList.stream().mapToDouble(Double::doubleValue).sum();
        double randomWeight = random.nextDouble() * totalWeight;

        for (int i = 0; i < weightsList.size(); i++) {
            randomWeight -= weightsList.get(i);
            if (randomWeight <= 0) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Gets the map of stations that are either empty or full, along with the
     * associated time.
     *
     * @return The map containing stations and associated time.
     */
    public Map<Station, Integer> getEmptyOrFullStations() {
        return this.emptyOrFullStations;
    }

    /**
     * Sets the current simulation time.
     *
     * @param time The simulation time to set.
     */
    public void setTime(int time) {
        this.timeNow = time;
    }

    /**
     * Gets the map of rentable objects that can be stolen, along with associated
     * information.
     *
     * @return The map containing rentable objects and associated information.
     */
    public Map<Rentable<?>, Integer[]> getCanBeStolen() {
        return this.canBeStolen;
    }

    /**
     * Gets the instance of the Repairer responsible for managing repairs.
     *
     * @return The Repairer instance.
     */
    public Repairer getRepairer() {
        return this.repairer;
    }

    /**
     * Gets the duration required for repairs.
     *
     * @return The duration required for repairs.
     */
    public int getReparationDuration() {
        return ControlCenter.repairDuration;
    }

}
