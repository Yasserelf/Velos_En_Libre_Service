package Staff;

public abstract class Staff {
    protected String type ;
    private static int idCounter=1 ; 
    protected int id;

    public Staff(String t) {
        this.type = t ; 
        this.id = idCounter++; 
    }

    public String getType() {
        return this.type;
    }

}
