package Strategie;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import Exceptions.EmptySpaceException;
import Exceptions.FullStationException;
import Exceptions.OutOfServiceException;
import Rentable.Rentable;
import Station.Station;

public class EqualRedistribution implements RedistributionStrategy {

    private Random random = new Random();
    private List<Rentable<?>> listRentable = new ArrayList<>();

    @Override
    public void redistribute(List<Station> stations)
            throws EmptySpaceException, OutOfServiceException, FullStationException {

        int numberOfStations = stations.size();
        int numberOfRentables = 0;
        int quantum = 0;

        for (Station s : stations) {
            numberOfRentables += s.getJustRentables().size();
        }

        if (numberOfStations != 0) {
            quantum = numberOfRentables / numberOfStations;
        }

        if (numberOfStations < 2) {
            return;
        }
        

        for (Station s : stations) {
            if (s.getJustRentables().size() > quantum) {
                int diff = s.getJustRentables().size() - quantum;
                List<Rentable<?>> removedRantbles = s.removeRandomRentables(diff);
                for (Rentable<?> r : removedRantbles) {
                    this.listRentable.add(r);
                }
            }
        }
       
        for (Station s : stations) {
            if (s.getJustRentables().size() < quantum) {
                if (this.getRentableList().size() > 0) {
                    int diff = quantum - s.getJustRentables().size();
                    this.setrentableList(s.addRentablesFrom(this.getRentableList(), diff));
                }
            }
        }

        while (!this.listRentable.isEmpty()) {
            Station s = stations.get(random.nextInt(stations.size()));
            int diff = s.getIdOfEmptyplaces().size();
            this.setrentableList(s.addRentablesFrom(this.getRentableList(), diff));
        }

    }

    private List<Rentable<?>> getRentableList() {
        return this.listRentable;
    }

    private void setrentableList(List<Rentable<?>> l) {
        this.listRentable = l;
    }

}
