public class Room {

    private int id;
    private int hotelId;
    private int roomNumber;
    private String type;
    private double price;
    private String status;

    // Used when ID is already known
    public Room(int id, int hotelId, int roomNumber,
                String type, double price, String status) {

        this.id = id;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    // Used when adding a new room
    // MySQL will generate the room ID
    public Room(int hotelId, int roomNumber,
                String type, double price, String status) {

        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String toString() {
        return "Room " + roomNumber
                + " (ID: " + id
                 + ", Hotel ID: " + hotelId + ")";
    }


}