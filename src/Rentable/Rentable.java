package Rentable;

import equipements.Status;

public class Rentable<T> {
    private int rentableId;
    private Status status;
    private int currentNbUse;
    private T theRentable;
    final static int MAXOFRENT = 5;

    private static int nextRentableId = 1;

    public Rentable(T theRentable) {
        this.rentableId = nextRentableId;
        this.status = Status.IN_SERVICE;
        this.currentNbUse = 0;
        this.theRentable = theRentable;
        nextRentableId++;
    }

    public int getId() {
        return this.rentableId;
    }

    public Status getStatus() {
        return this.status;
    }

    public int getCurrentNbUse() {
        return this.currentNbUse;
    }

    public T getTheRentable() {
        return this.theRentable;
    }

    public void setRentableId(int id) {
        this.rentableId = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCurrentNbUse(int n) {
        this.currentNbUse = n;
    }

    public void isOutOfService() {
        if (Rentable.MAXOFRENT <= this.getCurrentNbUse()) {
            this.markAsOutOfService();
        }
    }

    public void markAsOutOfService() {
        this.status = Status.OUT_OF_SERVICE;
    }

    public void incrementNbUse() {
        this.currentNbUse++;
        this.isOutOfService();
    }

    public void markAsStolen() {
        this.status = Status.STOLEN;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Rentable<?>))
            return false;
        Rentable<?> other = (Rentable<?>) o;
        return (this.rentableId == other.getId());
    }
}
