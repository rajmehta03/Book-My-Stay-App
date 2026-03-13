// UC6: Reservation Confirmation & Room Allocation

import java.util.*;

class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class RoomInventory {
    HashMap<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

public class mystayapp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v6.1");
        System.out.println("---------------------------------------");

        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Incoming booking requests
        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Suite Room"));
        bookingQueue.add(new Reservation("David", "Single Room"));

        RoomInventory inventory = new RoomInventory();

        // Track allocated room IDs
        Set<String> allocatedRoomIds = new HashSet<>();

        // Map room type -> assigned room IDs
        HashMap<String, Set<String>> roomAssignments = new HashMap<>();

        int roomCounter = 1;

        while (!bookingQueue.isEmpty()) {

            Reservation r = bookingQueue.poll();

            if (inventory.getAvailability(r.roomType) > 0) {

                String roomId = r.roomType.replace(" ", "") + "-" + roomCounter++;

                // Ensure uniqueness
                allocatedRoomIds.add(roomId);

                roomAssignments
                        .computeIfAbsent(r.roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.decrementRoom(r.roomType);

                System.out.println("Reservation Confirmed → Guest: "
                        + r.guestName + " | Room: " + roomId);

            } else {
                System.out.println("Reservation Failed → Guest: "
                        + r.guestName + " | No " + r.roomType + " available");
            }
        }

        System.out.println("\nAllocated Rooms:");
        for (String type : roomAssignments.keySet()) {
            System.out.println(type + " : " + roomAssignments.get(type));
        }
    }
}