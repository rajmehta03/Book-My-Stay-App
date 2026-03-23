import java.util.*;

// ---------------- BOOKING REQUEST ----------------
class BookingRequest {
    private String reservationId;
    private String roomType;

    public BookingRequest(String id, String type) {
        this.reservationId = id;
        this.roomType = type;
    }

    public String getReservationId() { return reservationId; }
    public String getRoomType() { return roomType; }
}

// ---------------- REQUEST QUEUE ----------------
class BookingRequestQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public void enqueue(BookingRequest req) {
        queue.add(req);
    }

    public BookingRequest dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ---------------- INVENTORY ----------------
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 3); // small count to show conflicts
        rooms.put("Double", 2);
    }

    public boolean allocateRoom(String type) {
        if (rooms.getOrDefault(type, 0) > 0) {
            rooms.put(type, rooms.get(type) - 1);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("\nFinal Inventory:");
        for (String key : rooms.keySet()) {
            System.out.println(key + " : " + rooms.get(key));
        }
    }
}

// ---------------- ALLOCATION SERVICE ----------------
class RoomAllocationService {
    public boolean allocate(String id, String type, RoomInventory inventory) {
        return inventory.allocateRoom(type);
    }
}

// ---------------- CONCURRENT PROCESSOR ----------------
class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService
    ) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {

        while (true) {

            BookingRequest request;

            // 🔒 Critical Section 1 (Queue Access)
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                request = bookingQueue.dequeue();
            }

            // 🔒 Critical Section 2 (Inventory Update)
            synchronized (inventory) {

                boolean success = allocationService.allocate(
                        request.getReservationId(),
                        request.getRoomType(),
                        inventory
                );

                if (success) {
                    System.out.println(Thread.currentThread().getName()
                            + " SUCCESS -> " + request.getReservationId());
                } else {
                    System.out.println(Thread.currentThread().getName()
                            + " FAILED -> " + request.getReservationId());
                }
            }
        }
    }
}

// ---------------- MAIN CLASS ----------------
public class mystayapp {

    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        // Add booking requests
        queue.enqueue(new BookingRequest("R1", "Single"));
        queue.enqueue(new BookingRequest("R2", "Single"));
        queue.enqueue(new BookingRequest("R3", "Single"));
        queue.enqueue(new BookingRequest("R4", "Single")); // may fail
        queue.enqueue(new BookingRequest("R5", "Double"));
        queue.enqueue(new BookingRequest("R6", "Double"));
        queue.enqueue(new BookingRequest("R7", "Double")); // may fail

        // Create threads
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(queue, inventory, service),
                "Thread-1"
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(queue, inventory, service),
                "Thread-2"
        );

        Thread t3 = new Thread(
                new ConcurrentBookingProcessor(queue, inventory, service),
                "Thread-3"
        );

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Show final inventory
        inventory.display();
    }
}