public class Room {
    int roomNumber;
    String type;
    boolean isBooked;
    double price;

    public Room(int roomNumber, String type, double price) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isBooked = false;
    }

    public String toString() {
        return "Room " + roomNumber + " | " + type + " | ₹" + price + " | " +
                (isBooked ? "Booked" : "Available");
    }
}