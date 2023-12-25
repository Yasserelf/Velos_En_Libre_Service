package Rentable;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import equipements.Status;

public class RentableTest {

    private Rentable<?> rentable;

    @BeforeEach
    public void init() {
        this.rentable = new Rentable<>(new ClassicBike());
    }

    @Test
    public void testIncrementNbUse() {

        assertEquals(0, this.rentable.getCurrentNbUse());
        assertEquals(Status.IN_SERVICE, this.rentable.getStatus());

        this.rentable.incrementNbUse();

        assertEquals(1, this.rentable.getCurrentNbUse());

        for (int i = 0; i <= Rentable.MAXOFRENT; i++) {
            this.rentable.incrementNbUse();
        }

        assertEquals(Status.OUT_OF_SERVICE, this.rentable.getStatus());
    }

    @Test
    public void testMarkAsStolen() {

        this.rentable.markAsStolen();

        assertEquals(Status.STOLEN, this.rentable.getStatus());
    }

    @Test
    public void testMarkAsOutOfService() {

        this.rentable.markAsOutOfService();

        assertEquals(Status.OUT_OF_SERVICE, this.rentable.getStatus());
    }

    @Test
    public void testEquals() {

        Rentable<?> rentable2 = new Rentable<>(new ClassicBike());

        assertFalse(this.rentable.equals(rentable2));
    }

}
