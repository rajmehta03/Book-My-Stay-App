// UC3: Centralized Room Inventory Management

import java.util.HashMap;

abstract class Room {
    String roomType;
    int beds;
    int size;
    double price;

    Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    void displayRoom() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: $" + price);
    }
}

class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 200, 100);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 350, 180);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 600, 350);
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Suite Room", 2);
    }

    int getAvailability(String roomType) {
        return inventory.get(roomType);
    }

    void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    void displayInventory() {
        System.out.println("Current Room Availability:");
        for (String room : inventory.keySet()) {
            System.out.println(room + " : " + inventory.get(room));
        }
    }
}

public class mystayapp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v3.1");
        System.out.println("---------------------------------------");

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();

        single.displayRoom();
        System.out.println("Available: " + inventory.getAvailability("Single Room"));
        System.out.println();

        doubleRoom.displayRoom();
        System.out.println("Available: " + inventory.getAvailability("Double Room"));
        System.out.println();

        suite.displayRoom();
        System.out.println("Available: " + inventory.getAvailability("Suite Room"));
        System.out.println();

        inventory.displayInventory();
    }
}