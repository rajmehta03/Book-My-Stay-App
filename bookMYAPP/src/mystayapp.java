import java.util.*;

// Booking class to store booking details
class Booking {
    String bookingId;
    String guestName;
    String roomType;
    String roomId;
    boolean isActive;

    public Booking(String bookingId, String guestName, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }
}

// Main Application Class
public class mystayapp {

    // Inventory: Room Type -> Count
    private static Map<String, Integer> inventory = new HashMap<>();

    // Available Rooms: Room Type -> List of Room IDs
    private static Map<String, Queue<String>> availableRooms = new HashMap<>();

    // Booking Records: Booking ID -> Booking Object
    private static Map<String, Booking> bookings = new HashMap<>();

    // Stack for rollback (LIFO)
    private static Stack<String> rollbackStack = new Stack<>();

    public static void main(String[] args) {

        // Initialize inventory and rooms
        initializeSystem();

        // Create sample booking
        createBooking("B101", "Arun", "Deluxe");
        createBooking("B102", "Priya", "Deluxe");

        // Cancel booking
        cancelBooking("B102");

        // Try invalid cancellation
        cancelBooking("B999");

        // Try duplicate cancellation
        cancelBooking("B102");

        // Display final state
        displayState();
    }

    // Initialize inventory and room IDs
    private static void initializeSystem() {
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);

        availableRooms.put("Deluxe", new LinkedList<>(Arrays.asList("D1", "D2")));
        availableRooms.put("Suite", new LinkedList<>(Arrays.asList("S1")));

        System.out.println("System Initialized.\n");
    }

    // Create booking
    private static void createBooking(String bookingId, String guestName, String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) == 0) {
            System.out.println("No rooms available for type: " + roomType);
            return;
        }

        String roomId = availableRooms.get(roomType).poll();
        inventory.put(roomType, inventory.get(roomType) - 1);

        Booking booking = new Booking(bookingId, guestName, roomType, roomId);
        bookings.put(bookingId, booking);

        System.out.println("Booking Confirmed: " + bookingId + " | Room: " + roomId);
    }

    // Cancel booking with rollback logic
    private static void cancelBooking(String bookingId) {
        System.out.println("\nAttempting cancellation for Booking ID: " + bookingId);

        // Validation: Check if booking exists
        if (!bookings.containsKey(bookingId)) {
            System.out.println("Cancellation Failed: Booking does not exist.");
            return;
        }

        Booking booking = bookings.get(bookingId);

        // Validation: Check if already cancelled
        if (!booking.isActive) {
            System.out.println("Cancellation Failed: Booking already cancelled.");
            return;
        }

        // Step 1: Record rollback (push room ID to stack)
        rollbackStack.push(booking.roomId);

        // Step 2: Restore inventory
        inventory.put(booking.roomType, inventory.get(booking.roomType) + 1);

        // Step 3: Release room back to availability
        availableRooms.get(booking.roomType).offer(booking.roomId);

        // Step 4: Update booking state
        booking.isActive = false;

        // Step 5: Log cancellation
        System.out.println("Cancellation Successful for Booking ID: " + bookingId);
        System.out.println("Room Released: " + booking.roomId);
    }

    // Display system state
    private static void displayState() {
        System.out.println("\n--- FINAL SYSTEM STATE ---");

        System.out.println("\nInventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }

        System.out.println("\nBookings:");
        for (Booking b : bookings.values()) {
            System.out.println(b.bookingId + " | " + b.guestName + " | " +
                    b.roomType + " | Room: " + b.roomId +
                    " | Active: " + b.isActive);
        }

        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}