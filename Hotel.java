public class Hotel {

    private int id;
    private String name;
    private String location;

    // Used when ID is already known
    public Hotel(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    // Used when adding a new hotel
    // MySQL will generate the ID
    public Hotel(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String toString(){
        return name + " -" + location +"(ID: \" + id + \")";
    }
    
    
}