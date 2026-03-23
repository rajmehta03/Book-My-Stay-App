import java.io.*;
import java.util.*;

// ---------------- RESERVATION ----------------
class Reservation implements Serializable {
    private String id, name, roomType;

    public Reservation(String id, String name, String roomType) {
        this.id = id;
        this.name = name;
        this.roomType = roomType;
    }

    public String getRoomType() { return roomType; }

    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Room: " + roomType;
    }
}

// ---------------- BOOKING HISTORY ----------------
class BookingHistory implements Serializable {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

// ---------------- INVENTORY ----------------
class RoomInventory implements Serializable {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 5);
        rooms.put("Double", 5);
        rooms.put("Suite", 3);
    }

    public boolean allocateRoom(String type) {
        if (rooms.getOrDefault(type, 0) > 0) {
            rooms.put(type, rooms.get(type) - 1);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (String key : rooms.keySet()) {
            System.out.println(key + " : " + rooms.get(key));
        }
    }
}

// ---------------- PERSISTENCE SERVICE ----------------
class PersistenceService {

    private static final String FILE_NAME = "hotel_data.ser";

    // Save data
    public static void save(BookingHistory history, RoomInventory inventory) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(history);
            out.writeObject(inventory);

            System.out.println("Data saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    // Load data
    public static Object[] load() {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            BookingHistory history = (BookingHistory) in.readObject();
            RoomInventory inventory = (RoomInventory) in.readObject();

            System.out.println("Data loaded successfully!");
            return new Object[]{history, inventory};

        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh...");
            return null;
        }
    }
}

// ---------------- MAIN CLASS ----------------
public class mystayapp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BookingHistory history;
        RoomInventory inventory;

        // ✅ LOAD DATA ON START
        Object[] data = PersistenceService.load();

        if (data != null) {
            history = (BookingHistory) data[0];
            inventory = (RoomInventory) data[1];
        } else {
            history = new BookingHistory();
            inventory = new RoomInventory();
        }

        while (true) {
            System.out.println("\n==== MENU ====");
            System.out.println("1. Book Room");
            System.out.println("2. View Bookings");
            System.out.println("3. View Inventory");
            System.out.println("4. Save & Exit");

            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1:
                    System.out.print("Enter ID: ");
                    String id = sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Room Type: ");
                    String room = sc.nextLine();

                    if (inventory.allocateRoom(room)) {
                        history.addReservation(new Reservation(id, name, room));
                        System.out.println("Booking successful!");
                    } else {
                        System.out.println("Room not available!");
                    }
                    break;

                case 2:
                    List<Reservation> list = history.getAllReservations();
                    if (list.isEmpty()) {
                        System.out.println("No bookings.");
                    } else {
                        for (Reservation r : list) {
                            System.out.println(r);
                        }
                    }
                    break;

                case 3:
                    inventory.display();
                    break;

                case 4:
                    // ✅ SAVE BEFORE EXIT
                    PersistenceService.save(history, inventory);
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}