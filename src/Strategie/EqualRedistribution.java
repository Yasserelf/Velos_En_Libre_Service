package Strategie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Exceptions.EmptySpaceException;
import Exceptions.FullStationException;
import Exceptions.OutOfServiceException;
import Rentable.Rentable;
import Station.Station;

/**
 * Implementation of the {@link RedistributionStrategy} interface that performs
 * equal redistribution of rentable items.
 */
public class EqualRedistribution implements RedistributionStrategy {

    private Random random = new Random();
    private List<Rentable<?>> listRentable = new ArrayList<>();

    /**
     * Redistributes rentable items equally among a list of stations.
     *
     * @param stations The list of stations to redistribute rentable items among.
     * @throws EmptySpaceException   If there is an empty space exception during
     *                               redistribution.
     * @throws OutOfServiceException If there is an out-of-service exception during
     *                               redistribution.
     * @throws FullStationException  If there is a full station exception during
     *                               redistribution.
     */
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
                List<Rentable<?>> removedRentables = s.removeRandomRentables(diff);
                listRentable.addAll(removedRentables);
            }
        }

        for (Station s : stations) {
            if (s.getJustRentables().size() < quantum) {
                if (!listRentable.isEmpty()) {
                    int diff = quantum - s.getJustRentables().size();
                    listRentable = s.addRentablesFrom(listRentable, diff);
                }
            }
        }

        while (!listRentable.isEmpty()) {
            Station s = stations.get(random.nextInt(stations.size()));
            int diff = s.getIdOfEmptyplaces().size();
            listRentable = s.addRentablesFrom(listRentable, diff);
        }
    }
}
