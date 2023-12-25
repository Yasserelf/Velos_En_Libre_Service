package ControlCenter;

import java.util.List;

import Exceptions.EmptySpaceException;
import Exceptions.FullStationException;
import Exceptions.OutOfServiceException;
import Station.Station;
import Strategie.RedistributionStrategy;

public class MockEqualRedistribution implements RedistributionStrategy {
    private int checkIfRedistribute = 0;

    @Override
    public void redistribute(List<Station> stations)
            throws EmptySpaceException, OutOfServiceException, FullStationException {
        this.checkIfRedistribute++;
    }

    public int getcheckIfRedistribute() {
        return this.checkIfRedistribute;
    }

}
