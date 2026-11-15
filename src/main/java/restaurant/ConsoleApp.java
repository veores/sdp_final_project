package restaurant.console;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import restaurant.decorator.Extra;
import restaurant.facade.ItemRequest;
import restaurant.facade.RestaurantFacade;
import restaurant.factory.MealFactory;
import restaurant.factory.MealType;
import restaurant.model.OrderStatus;
import restaurant.order.Order;
import restaurant.strategy.DiscountStrategy;
import restaurant.strategy.FixedAmountDiscount;
import restaurant.strategy.HappyHourDiscount;
import restaurant.strategy.NoDiscount;
import restaurant.strategy.PercentageDiscount;

public class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final RestaurantFacade restaurant = new RestaurantFacade();
    private final Map<Integer, Order> sessionOrders = new LinkedHashMap<>();

    public static void main(String[] args) {
        new ConsoleApp().run();
    }

    private void run() {
        System.out.println("============================================");
        System.out.println("       RESTAURANT ORDERING SYSTEM");
        System.out.println("          INTERACTIVE CONSOLE");
        System.out.println("============================================");

        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ", 0, 6);

            try {
                switch (choice) {
                    case 1 -> createOrder(false);
                    case 2 -> createOrder(true);
                    case 3 -> showOrders();
                    case 4 -> manageOrder();
                    case 5 -> showNotifications();
                    case 6 -> showMenu();
                    case 0 -> running = false;
                    default -> System.out.println("Unknown option.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Operation failed: " + ex.getMessage());
            }

            System.out.println();
        }

        System.out.println("Thank you for using the restaurant system!");
    }

    private void printMenu() {
        System.out.println("\n--------------- MAIN MENU ---------------");
        System.out.println("1. Create dine-in order");
        System.out.println("2. Create takeaway order");
        System.out.println("3. Show session orders");
        System.out.println("4. Manage an order");
        System.out.println("5. Show Observer notifications");
        System.out.println("6. Show food and extras menu");
        System.out.println("0. Exit");
        System.out.println("-----------------------------------------");
    }

    private void createOrder(boolean takeaway) {
        System.out.println("\n--- CREATE " + (takeaway ? "TAKEAWAY" : "DINE-IN") + " ORDER ---");

        String customer = readNonBlank("Customer name: ");
        int table = takeaway ? 0 : readInt("Table number: ", 1, 999);
        List<ItemRequest> requests = new ArrayList<>();
        boolean adding = true;

        while (adding) {
            showMenu();
            MealType type = readMealType();
            List<Extra> extras = readExtras();
            requests.add(ItemRequest.of(type, extras.toArray(new Extra[0])));
            adding = readYesNo("Add another meal? (y/n): ");
        }

        DiscountStrategy discount = chooseDiscount();
        ItemRequest[] requestArray = requests.toArray(new ItemRequest[0]);
        Order order = takeaway
                ? restaurant.placeTakeaway(customer, discount, requestArray)
                : restaurant.placeOrder(customer, table, discount, requestArray);

        sessionOrders.put(order.getId(), order);
        System.out.println("\nOrder created successfully!");
        System.out.println(restaurant.receipt(order.getId()));
    }

    private MealType readMealType() {
        MealType[] types = MealType.values();
        System.out.println("Choose a meal:");

        for (int i = 0; i < types.length; i++) {
            System.out.printf("%d. %s - $%.2f%n", i + 1, types[i], MealFactory.create(types[i]).getPrice());
        }

        int choice = readInt("Meal number: ", 1, types.length);
        return types[choice - 1];
    }

    private List<Extra> readExtras() {
        Extra[] extras = Extra.values();
        List<Extra> selected = new ArrayList<>();

        System.out.println("Available extras (you can add several):");
        for (int i = 0; i < extras.length; i++) {
            System.out.printf("%d. %s%n", i + 1, extras[i]);
        }
        System.out.println("0. No extra / finish selection");

        while (true) {
            int choice = readInt("Extra number: ", 0, extras.length);
            if (choice == 0) {
                break;
            }

            selected.add(extras[choice - 1]);
            System.out.println(extras[choice - 1] + " added.");
            if (!readYesNo("Add one more extra? (y/n): ")) {
                break;
            }
        }

        return selected;
    }

    private DiscountStrategy chooseDiscount() {
        System.out.println("\nChoose discount strategy:");
        System.out.println("0. No discount");
        System.out.println("1. Percentage discount");
        System.out.println("2. Fixed amount discount");
        System.out.println("3. Happy Hour (16:00-18:00, 20%)");

        int choice = readInt("Discount: ", 0, 3);
        return switch (choice) {
            case 1 -> new PercentageDiscount("Custom", readDouble("Discount percent (0-100): ", 0, 100));
            case 2 -> new FixedAmountDiscount("Coupon", readDouble("Discount amount ($): ", 0, 1_000_000));
            case 3 -> new HappyHourDiscount();
            default -> new NoDiscount();
        };
    }

    private void showOrders() {
        System.out.println("\n--- SESSION ORDERS ---");
        if (sessionOrders.isEmpty()) {
            System.out.println("No orders have been created in this session.");
            return;
        }

        for (Order order : sessionOrders.values()) {
            String place = order.isTakeaway() ? "Takeaway" : "Table " + order.getTableNumber();
            System.out.printf("#%d | %-18s | %-12s | %s | Total: $%.2f%n", order.getId(), order.getCustomerName(), place, order.getStatus(), order.getTotal());
        }
    }

    private void manageOrder() {
        if (sessionOrders.isEmpty()) {
            System.out.println("There are no orders to manage yet.");
            return;
        }

        showOrders();
        int id = readInt("Enter order ID: ", 1, Integer.MAX_VALUE);
        Order order = sessionOrders.get(id);
        if (order == null) {
            System.out.println("This order is not part of this console session.");
            return;
        }

        boolean managing = true;
        while (managing) {
            System.out.println("\nOrder #" + id + " | Current status: " + order.getStatus());
            System.out.println("1. Advance to next status");
            System.out.println("2. Cancel order");
            System.out.println("3. Print receipt");
            System.out.println("4. Back to main menu");
            int action = readInt("Choose: ", 1, 4);

            switch (action) {
                case 1 -> {
                    if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
                        System.out.println("Order is already in a final state.");
                    } else {
                        restaurant.advance(id);
                        System.out.println("New status: " + order.getStatus());
                    }
                }
                case 2 -> {
                    if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
                        System.out.println("Cannot cancel a final-state order.");
                    } else if (readYesNo("Are you sure? (y/n): ")) {
                        restaurant.cancel(id);
                        System.out.println("Order cancelled.");
                    }
                }
                case 3 -> System.out.println(restaurant.receipt(id));
                case 4 -> managing = false;
            }
        }
    }

    private void showNotifications() {
        System.out.println("\n--- KITCHEN DISPLAY ---");
        printMessages(restaurant.getKitchenDisplay().getMessages());
        System.out.println("\n--- CUSTOMER NOTIFICATIONS ---");
        printMessages(restaurant.getCustomerNotifier().getMessages());
    }

    private void printMessages(List<String> messages) {
        if (messages.isEmpty()) {
            System.out.println("No notifications yet.");
            return;
        }

        for (String message : messages) {
            System.out.println(message);
        }
    }

    private void showMenu() {
        System.out.println("\n--- FOOD MENU ---");
        for (MealType type : MealType.values()) {
            var item = MealFactory.create(type);
            System.out.printf("%-8s | %-20s | $%.2f | %s%n", type, item.getName(), item.getPrice(), item.getDescription());
        }

        System.out.println("--- EXTRAS ---");
        System.out.println("CHEESE $1.50 | BACON $2.00 | MUSHROOMS $1.00 | SPICY_SAUCE $0.50");
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Show the validation message below.
            }

            System.out.printf("Enter a whole number from %d to %d.%n", min, max);
        }
    }

    private double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().replace(',', '.');

            try {
                double value = Double.parseDouble(input);
                if (Double.isFinite(value) && value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Show the validation message below.
            }

            System.out.printf("Enter a number from %.2f to %.2f.%n", min, max);
        }
    }

    private String readNonBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isBlank()) {
                return value;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim().toLowerCase();
            if (value.equals("y") || value.equals("yes")) {
                return true;
            }
            if (value.equals("n") || value.equals("no")) {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }
}