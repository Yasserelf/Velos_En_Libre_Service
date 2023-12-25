package Strategie;

import java.util.List;

import Exceptions.EmptySpaceException;
import Exceptions.FullStationException;
import Exceptions.OutOfServiceException;
import Rentable.Rentable;
import Station.Station;

public interface RedistributionStrategy {

    void redistribute(List<Station> stations)
            throws EmptySpaceException, OutOfServiceException, FullStationException;

}
