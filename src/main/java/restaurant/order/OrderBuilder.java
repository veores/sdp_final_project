package restaurant.order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import restaurant.model.MenuItem;

public class OrderBuilder {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1001);

    private String customerName;
    private int tableNumber;
    private boolean takeaway;
    private String note = "";
    private final List<MenuItem> items = new ArrayList<>();

    public OrderBuilder customer(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public OrderBuilder table(int tableNumber) {
        this.tableNumber = tableNumber;
        return this;
    }

    public OrderBuilder takeaway(boolean takeaway) {
        this.takeaway = takeaway;
        return this;
    }

    public OrderBuilder note(String note) {
        this.note = note == null ? "" : note;
        return this;
    }

    public OrderBuilder addItem(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null");
        }
        items.add(item);
        return this;
    }

    public Order build() {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalStateException("Customer name is required");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must contain at least one item");
        }
        if (!takeaway && tableNumber <= 0) {
            throw new IllegalStateException("Dine-in order needs a table number");
        }
        return new Order(NEXT_ID.getAndIncrement(), customerName.trim(),
                takeaway ? 0 : tableNumber, takeaway, note, items);
    }
}
