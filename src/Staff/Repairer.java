package Staff;

import Rentable.Rentable;
import equipements.Status;
import java.util.*;

/**
 * A staff member responsible for repairing rentable objects.
 * The Repairer can add rentable objects to the repair queue, remove them once repaired,
 * and check which objects have completed repairs to bring them back into service.
 */
public class Repairer extends Staff {

    /**
     * A map to track rentable objects currently undergoing repairs
     * and the expected time for them to be back in service.
     */
    private Map<Rentable<?>, Integer> onRepair;

    /**
     * Constructs a new Repairer staff member.
     * The type is set to "Repairer", and the repair queue is initialized.
     */
    public Repairer() {
        super("Repairer");
        this.onRepair = new HashMap<>();
    }

    /**
     * Gets the map of rentable objects currently undergoing repairs.
     *
     * @return A map containing rentable objects and their expected time to be back in service.
     */
    public Map<Rentable<?>, Integer> getOnRepair() {
        return this.onRepair;
    }

    /**
     * Adds a rentable object to the repair queue with the specified expected time for repair completion.
     *
     * @param r         The rentable object to be repaired.
     * @param timeBack  The expected time for the rentable object to be back in service.
     */
    public void addOnRepair(Rentable<?> r, Integer timeBack) {
        onRepair.put(r, timeBack);
    }

    /**
     * Removes a rentable object from the repair queue once it has been repaired.
     *
     * @param r The rentable object that has been repaired.
     */
    public void delOnRepair(Rentable<?> r) {
        onRepair.remove(r);
    }

    /**
     * Checks the repair queue to determine which rentable objects have completed repairs
     * and updates their status to be back in service.
     *
     * @param timeNow The current simulation time.
     * @return The number of rentable objects that have completed repairs.
     */
    public int checkWhoSBack(int timeNow) {
        int repairedCount = 0;
        for (Rentable<?> r : this.onRepair.keySet()) {
            if (timeNow == onRepair.get(r)) {
                repairedCount++;
                r.setStatus(Status.IN_SERVICE);
            }
        }
        return repairedCount;
    }
}
