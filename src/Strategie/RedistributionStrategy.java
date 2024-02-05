package Strategie;

import java.util.List;

import Exceptions.EmptySpaceException;
import Exceptions.FullStationException;
import Exceptions.OutOfServiceException;
import Station.Station;

/**
 * An interface representing a redistribution strategy for stations.
 * Implementations of this interface define how the redistribution of rentable items
 * is performed among a list of stations.
 */
public interface RedistributionStrategy {

    /**
     * Redistributes rentable items among a list of stations.
     *
     * @param stations The list of stations to redistribute rentable items among.
     * @throws EmptySpaceException   If there is an empty space exception during redistribution.
     * @throws OutOfServiceException If there is an out-of-service exception during redistribution.
     * @throws FullStationException  If there is a full station exception during redistribution.
     */
    void redistribute(List<Station> stations)
            throws EmptySpaceException, OutOfServiceException, FullStationException;
}
