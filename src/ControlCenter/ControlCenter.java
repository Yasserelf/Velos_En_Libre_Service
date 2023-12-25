package ControlCenter;

import Rentable.*;
import Station.Station;
import Strategie.EqualRedistribution;
import Strategie.RedistributionStrategy;
import Staff.Repairer;

import static org.junit.Assert.fail;

import java.util.*;
import equipements.Status;
import Exceptions.AlreadyDepositException;
import Exceptions.AlreadyRentedException;
import Exceptions.EmptySpaceException;
import Exceptions.FullStationException;
import Exceptions.OutOfServiceException;

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

    public static ControlCenter getControlCenter() {
        if (controlCenter == null) {
            controlCenter = new ControlCenter();
        }
        return controlCenter;
    }

    public Map<Rentable<?>, Integer[]> getCanBeStolen() {
        return this.canBeStolen;
    }

    public List<Rentable<?>> getAllRentables() {
        return this.allRentables;
    }

    public Map<Station, Integer> getEmptyOrFullStations() {
        return this.emptyOrFullStations;
    }

    public int getRedistributionDuration() {
        return ControlCenter.redistributionDuration;
    }

    public Repairer getRepairer() {
        return this.repairer;
    }

    public int getReparationDuration() {
        return ControlCenter.repairDuration;
    }

    public void setRedistributionStrategy(RedistributionStrategy strategy) {
        this.redistributionStrategy = strategy;
    }

    public List<Station> getStationList() {
        return this.stationList;
    }

    public List<Rentable<?>> getRentedList() {
        return this.rentedList;
    }

    public void setAllRentables(List<Rentable<?>> allRentables) {
        this.allRentables = allRentables;
    }

    public void setRedistributionDuration(int duration) {
        ControlCenter.redistributionDuration = duration;
    }

    public void setStationList(List<Station> stations) {
        this.stationList = stations;
    }

    public void setRentedList(List<Rentable<?>> rentedList) {
        this.rentedList = rentedList;
    }

    public void addStation(Station s) {
        if (!this.stationList.contains(s))
            this.stationList.add(s);
    }

    public void setTime(int time) {
        this.timeNow = time;
    }

    public int checkTime() {
        return this.timeNow;
    }

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
                System.out.println("Redistribution started,  here is the state of stations after redistribution : ");
                for (Station stat : this.stationList) {
                    System.out.println("Station Name: " + stat.getStationId()
                            + " Number of available for rent Rentables is : " + stat.getJustRentables().size());
                }
                System.out.println("-------------------------");
                this.red++;
                this.emptyOrFullStations.clear();
                break;
            }
        }
    }

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
    }

    public void sendToRepair(Rentable<?> r) {
        this.repairer.addOnRepair(r, this.timeNow + ControlCenter.repairDuration);
        this.nbOfTotalRepairRound++;
    }

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

    public void printStatistics(int nbRepaired) {
        for (Station s : this.stationList) {
            System.out.println("Station Name: " + s.getStationId() + " Rented from : " + s.getRendtedRound()
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

    public void simulate() throws AlreadyRentedException, EmptySpaceException, OutOfServiceException,
            AlreadyDepositException, FullStationException {
        int randomLocation;
        int res;

        while (true) {
            res = random.nextInt(this.stationList.size() / 2, this.stationList.size());
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

            if (this.red == 200) {
                fail();
            }

            /*
             * try {
             * Thread.sleep(1000);
             * } catch (InterruptedException e) {
             * e.printStackTrace();
             * }
             */

        }

    }

    private int getRandomWeightedStationIndex(int choix) {
        List<Double> weightsList;
        List<Double> weightsListdep = new ArrayList<>();
        List<Double> weightsListrent = new ArrayList<>();
        int BigStations = this.stationList.size() / 5;
        int j = 0;
        for (Station s : this.stationList) {
            if (j < BigStations) {
                weightsListrent.add(random.nextDouble(3, 6));
                weightsListdep.add(random.nextDouble(0, 2));
            } else {
                weightsListrent.add(random.nextDouble(0, 2));
                weightsListdep.add(random.nextDouble(3, 6));
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

}
