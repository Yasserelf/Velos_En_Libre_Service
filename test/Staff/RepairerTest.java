package Staff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import Rentable.ClassicBike;
import Rentable.ElectricBike;
import Rentable.Rentable;

public class RepairerTest {

    @Test
    public void testAddOnRepair() {
        Repairer repairer = new Repairer();
        Rentable<?> rentable1 = new Rentable<>(new ElectricBike());
        Rentable<?> rentable2 = new Rentable<>(new ClassicBike());

        repairer.addOnRepair(rentable1, 10);
        repairer.addOnRepair(rentable2, 15);

        assertEquals(2, repairer.getOnRepair().size());
        assertTrue(repairer.getOnRepair().containsKey(rentable1));
        assertTrue(repairer.getOnRepair().containsKey(rentable2));
    }

    @Test
    public void testDelOnRepair() {
        Repairer repairer = new Repairer();
        Rentable<?> rentable1 = new Rentable<>(new ElectricBike());
        Rentable<?> rentable2 = new Rentable<>(new ClassicBike());

        repairer.addOnRepair(rentable1, 10);
        repairer.addOnRepair(rentable2, 15);

        assertEquals(2, repairer.getOnRepair().size());
        assertTrue(repairer.getOnRepair().containsKey(rentable1));
        assertTrue(repairer.getOnRepair().containsKey(rentable2));

        repairer.delOnRepair(rentable1);

        assertEquals(1, repairer.getOnRepair().size());
        assertFalse(repairer.getOnRepair().containsKey(rentable1));
        assertTrue(repairer.getOnRepair().containsKey(rentable2));
    }

    @Test
    public void testCheckWhoSBack() {
        Repairer repairer = new Repairer();
        Rentable<?> rentable1 = new Rentable<>(new ElectricBike());
        Rentable<?> rentable2 = new Rentable<>(new ClassicBike());

        repairer.addOnRepair(rentable1, 10);
        repairer.addOnRepair(rentable2, 15);

        assertEquals(0, repairer.checkWhoSBack(9));
        assertEquals(1, repairer.checkWhoSBack(10));
        assertEquals(0, repairer.checkWhoSBack(14));
        assertEquals(1, repairer.checkWhoSBack(15));
    }

}
