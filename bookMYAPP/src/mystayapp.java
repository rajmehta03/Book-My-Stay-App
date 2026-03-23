import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String msg) {
        super(msg);
    }
}

// Reservation Class
class Reservation {
    private String id, name, roomType;

    public Reservation(String id, String name, String roomType) {
        this.id = id;
        this.name = name;
        this.roomType = roomType;
    }

    public String getId() { return id; }
    public String getRoomType() { return roomType; }

    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Room: " + roomType;
    }
}


class ReservationValidator {

    private static final List<String> validRooms =
            Arrays.asList("Single", "Double", "Suite");

    public static void validate(String id, String name, String roomType)
            throws InvalidBookingException {

        if (id == null || id.trim().isEmpty())
            throw new InvalidBookingException("Reservation ID cannot be empty.");

        if (name == null || name.trim().isEmpty())
            throw new InvalidBookingException("Guest name cannot be empty.");

        if (!validRooms.contains(roomType))
            throw new InvalidBookingException(
                    "Invalid room type! Allowed: " + validRooms);
    }
}

// Booking History
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Report Service
class BookingReportService {
    public void generateReport(BookingHistory history) {
        List<Reservation> list = history.getAllReservations();

        if (list.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        System.out.println("\n===== REPORT =====");
        System.out.println("Total Bookings: " + list.size());

        Map<String, Integer> roomCount = new HashMap<>();
        for (Reservation r : list) {
            roomCount.put(r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("Room Type Summary:");
        for (String key : roomCount.keySet()) {
            System.out.println(key + " : " + roomCount.get(key));
        }
    }
}

// Add-On Service
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public double getCost() { return cost; }

    public String toString() {
        return name + " (₹" + cost + ")";
    }
}

// Add-On Manager
class AddOnServiceManager {
    private Map<String, List<AddOnService>> map = new HashMap<>();

    public void addServices(String id, List<AddOnService> services) {
        map.putIfAbsent(id, new ArrayList<>());
        map.get(id).addAll(services);
    }

    public List<AddOnService> getServices(String id) {
        return map.getOrDefault(id, new ArrayList<>());
    }

    public double getTotalCost(String id) {
        double total = 0;
        for (AddOnService s : getServices(id)) {
            total += s.getCost();
        }
        return total;
    }
}

// Main App
public class mystayapp {

    public static void showWelcome() {
        System.out.println("=================================");
        System.out.println("      Welcome to Book My Stay    ");
        System.out.println("=================================");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BookingHistory history = new BookingHistory();
        BookingReportService report = new BookingReportService();
        AddOnServiceManager addOnManager = new AddOnServiceManager();

        List<AddOnService> services = Arrays.asList(
                new AddOnService("Breakfast", 500),
                new AddOnService("Airport Pickup", 1200),
                new AddOnService("Extra Bed", 800),
                new AddOnService("Spa", 1500)
        );

        showWelcome();

        while (true) {
            System.out.println("\n====== MENU ======");
            System.out.println("1. Confirm Booking");
            System.out.println("2. Add Add-On Services");
            System.out.println("3. View Add-On Services");
            System.out.println("4. View Booking History");
            System.out.println("5. Generate Report");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");

            try {
                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {

                    case 1:
                        System.out.print("Enter ID: ");
                        String id = sc.nextLine();

                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Room Type (Single/Double/Suite): ");
                        String room = sc.nextLine();

                        // Validation
                        ReservationValidator.validate(id, name, room);

                        Reservation r = new Reservation(id, name, room);
                        history.addReservation(r);

                        System.out.println("Booking Confirmed!");
                        break;

                    case 2:
                        System.out.print("Enter Reservation ID: ");
                        String rid = sc.nextLine();

                        System.out.println("Available Services:");
                        for (int i = 0; i < services.size(); i++) {
                            System.out.println((i + 1) + ". " + services.get(i));
                        }

                        System.out.print("How many services: ");
                        int n = sc.nextInt();

                        List<AddOnService> selected = new ArrayList<>();
                        for (int i = 0; i < n; i++) {
                            System.out.print("Choose: ");
                            int s = sc.nextInt();

                            if (s >= 1 && s <= services.size()) {
                                selected.add(services.get(s - 1));
                            } else {
                                System.out.println("Invalid service choice!");
                            }
                        }

                        addOnManager.addServices(rid, selected);
                        System.out.println("Services Added!");
                        sc.nextLine();
                        break;

                    case 3:
                        System.out.print("Enter Reservation ID: ");
                        String vid = sc.nextLine();

                        List<AddOnService> list = addOnManager.getServices(vid);
                        if (list.isEmpty()) {
                            System.out.println("No services found.");
                        } else {
                            for (AddOnService s : list) {
                                System.out.println(s);
                            }
                            System.out.println("Total Cost: ₹" +
                                    addOnManager.getTotalCost(vid));
                        }
                        break;

                    case 4:
                        List<Reservation> all = history.getAllReservations();
                        if (all.isEmpty()) {
                            System.out.println("No bookings.");
                        } else {
                            for (Reservation res : all) {
                                System.out.println(res);
                            }
                        }
                        break;

                    case 5:
                        report.generateReport(history);
                        break;

                    case 6:
                        System.out.println("Thank you!");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (InvalidBookingException e) {
                // Custom validation error
                System.out.println("Error: " + e.getMessage());

            } catch (InputMismatchException e) {
                //  Wrong input type
                System.out.println("Invalid input! Please enter correct data.");
                sc.nextLine(); // clear buffer

            } catch (Exception e) {
                //  Safety net
                System.out.println("Unexpected error occurred.");
            }
        }
    }
}