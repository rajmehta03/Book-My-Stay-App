import java.util.*;

// Add-On Service Class
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public String toString() {
        return name + " (₹" + cost + ")";
    }
}

// Manager Class
class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    public void addServices(String reservationId, List<AddOnService> services) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).addAll(services);
    }

    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        for (AddOnService s : getServices(reservationId)) {
            total += s.getCost();
        }
        return total;
    }
}

// Main App
public class mystayapp {

    public static void showWelcomeMessage() {
        System.out.println("=================================");
        System.out.println("      Welcome to Book My Stay    ");
        System.out.println("     Hotel Booking System v1.0   ");
        System.out.println("=================================");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        showWelcomeMessage();

        System.out.print("\nEnter Reservation ID: ");
        String reservationId = sc.nextLine();

        // Available services
        List<AddOnService> services = Arrays.asList(
                new AddOnService("Breakfast", 500),
                new AddOnService("Airport Pickup", 1200),
                new AddOnService("Extra Bed", 800),
                new AddOnService("Spa Access", 1500)
        );

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Add Services");
            System.out.println("2. View Services");
            System.out.println("3. View Total Cost");
            System.out.println("4. Exit");

            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("\nAvailable Services:");
                    for (int i = 0; i < services.size(); i++) {
                        System.out.println((i + 1) + ". " + services.get(i));
                    }

                    System.out.print("How many services to add: ");
                    int n = sc.nextInt();

                    List<AddOnService> selected = new ArrayList<>();

                    for (int i = 0; i < n; i++) {
                        System.out.print("Enter service number: ");
                        int s = sc.nextInt();

                        if (s >= 1 && s <= services.size()) {
                            selected.add(services.get(s - 1));
                        } else {
                            System.out.println("Invalid choice!");
                        }
                    }

                    manager.addServices(reservationId, selected);
                    System.out.println("Services added successfully!");
                    break;

                case 2:
                    System.out.println("\nSelected Services:");
                    for (AddOnService s : manager.getServices(reservationId)) {
                        System.out.println("- " + s);
                    }
                    break;

                case 3:
                    double total = manager.calculateTotalCost(reservationId);
                    System.out.println("Total Add-On Cost: ₹" + total);
                    break;

                case 4:
                    System.out.println("Thank you for using Book My Stay!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}