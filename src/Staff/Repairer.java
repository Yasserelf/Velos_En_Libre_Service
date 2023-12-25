package Staff;

import Rentable.Rentable;
import equipements.Status;
import java.util.*;

public class Repairer extends Staff {

    private Map<Rentable<?>, Integer> onRepair;

    public Repairer() {
        super("Repairer");
        this.onRepair = new HashMap<>();
    }

    public Map<Rentable<?>, Integer> getOnRepair() {
        return this.onRepair;
    }

    public void addOnRepair(Rentable<?> r, Integer timeBack) {
        onRepair.put(r, timeBack);
    }

    public void delOnRepair(Rentable<?> r) {
        onRepair.remove(r);
    }

    public int checkWhoSBack(int timeNow) {
        int i= 0;
        for (Rentable<?> r : this.onRepair.keySet()) {
            if (timeNow == onRepair.get(r)) {
                i++;
                r.setStatus(Status.IN_SERVICE);
            }
        }
        return i;
    }
}
