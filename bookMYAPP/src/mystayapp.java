// UC2: Basic Room Types & Static Availability

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

public class mystayapp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v2.1");
        System.out.println("---------------------------------------");

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 10;
        int doubleAvailable = 5;
        int suiteAvailable = 2;

        single.displayRoom();
        System.out.println("Available: " + singleAvailable);
        System.out.println();

        doubleRoom.displayRoom();
        System.out.println("Available: " + doubleAvailable);
        System.out.println();

        suite.displayRoom();
        System.out.println("Available: " + suiteAvailable);
    }
}