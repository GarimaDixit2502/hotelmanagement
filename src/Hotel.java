import java.util.ArrayList;

public class Hotel {
    ArrayList<Room> rooms = new ArrayList<>();

    // FIXED PRICING LOGIC
    public double getPrice(String type) {
        switch (type.toLowerCase()) {
            case "standard non ac": return 500;
            case "standard ac": return 1000;
            case "deluxe non ac": return 2000;
            case "deluxe ac": return 3000;
            case "suite non ac": return 5000;
            case "suite ac": return 6000;
        }
        return 0;
    }

    public boolean addRoom(int number, String type) {
        for (Room r : rooms) {
            if (r.roomNumber == number) return false;
        }
        double price = getPrice(type);
        rooms.add(new Room(number, type, price));
        return true;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public boolean bookRoom(int number) {
        for (Room r : rooms) {
            if (r.roomNumber == number && !r.isBooked) {
                r.isBooked = true;
                return true;
            }
        }
        return false;
    }

    public ArrayList<Room> getAvailableRooms() {
        ArrayList<Room> list = new ArrayList<>();
        for (Room r : rooms) if (!r.isBooked) list.add(r);
        return list;
    }

    public ArrayList<Room> getBookedRooms() {
        ArrayList<Room> list = new ArrayList<>();
        for (Room r : rooms) if (r.isBooked) list.add(r);
        return list;
    }

    public double checkoutRoom(int number) {
        for (Room r : rooms) {
            if (r.roomNumber == number && r.isBooked) {
                r.isBooked = false;
                return r.price;
            }
        }
        return 0;
    }
}