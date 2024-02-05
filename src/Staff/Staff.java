package Staff;

/**
 * The abstract base class for different types of staff members.
 * It provides a mechanism to assign a unique identifier to each staff instance.
 */
public abstract class Staff {
    /**
     * The type of the staff member.
     */
    protected String type;

    /**
     * A unique identifier for each staff member.
     */
    private static int idCounter = 1;

    /**
     * The unique identifier for this staff member.
     */
    protected int id;

    /**
     * Constructs a new staff member with the given type.
     * The unique identifier is automatically assigned.
     *
     * @param t The type of the staff member.
     */
    public Staff(String t) {
        this.type = t;
        this.id = idCounter++;
    }

    /**
     * Gets the type of the staff member.
     *
     * @return The type of the staff member.
     */
    public String getType() {
        return this.type;
    }
}
