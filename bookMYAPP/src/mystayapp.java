// UC5: Booking Request (First-Come-First-Served)

import java.util.*;

class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

public class mystayapp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v5.1");
        System.out.println("---------------------------------------");

        // Booking request queue (FIFO)
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Guest booking requests
        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Suite Room"));
        bookingQueue.add(new Reservation("David", "Single Room"));

        System.out.println("Booking Requests in Queue (First-Come-First-Served):");
        System.out.println("----------------------------------------------------");

        // Display queue without processing
        for (Reservation r : bookingQueue) {
            r.display();
        }

        System.out.println("\nTotal Requests Waiting: " + bookingQueue.size());
    }
}