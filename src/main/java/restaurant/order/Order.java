package restaurant.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import restaurant.model.MenuItem;
import restaurant.model.OrderStatus;
import restaurant.strategy.DiscountStrategy;
import restaurant.strategy.NoDiscount;
import restaurant.observer.OrderObserver;

public class Order {
    private final int id;
    private final String customerName;
    private final int tableNumber;
    private final boolean takeaway;
    private final String note;
    private final List<MenuItem> items;
    private OrderStatus status = OrderStatus.NEW;
    private DiscountStrategy discountStrategy = new NoDiscount();
    private final List<OrderObserver> observers = new ArrayList<>();

    Order(int id, String customerName, int tableNumber, boolean takeaway, String note, List<MenuItem> items) {
        this.id = id;
        this.customerName = customerName;
        this.tableNumber = tableNumber;
        this.takeaway = takeaway;
        this.note = note;
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public boolean isTakeaway() {
        return takeaway;
    }

    public String getNote() {
        return note;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double getSubtotal() {
        double sum = 0;
        for (MenuItem item : items) {
            sum += item.getPrice();
        }
        return round2(sum);
    }

    public DiscountStrategy getDiscountStrategy() {
        return discountStrategy;
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        if (discountStrategy == null) {
            throw new IllegalArgumentException("Discount strategy must not be null");
        }
        this.discountStrategy = discountStrategy;
    }

    public double getDiscountAmount() {
        return round2(getSubtotal() - getTotal());
    }

    public double getTotal() {
        return round2(discountStrategy.apply(getSubtotal()));
    }

    public void setStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order #" + id + " is already " + status);
        }
        OrderStatus old = status;
        status = newStatus;
        notifyObservers(old, newStatus);
    }

    public void advance() {
        setStatus(status.next());
    }

    public void cancel() {
        setStatus(OrderStatus.CANCELLED);
    }


    public void attach(OrderObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(OrderObserver observer) {
        observers.remove(observer);
    }

    public void announce() {
        notifyObservers(null, status);
    }

    private void notifyObservers(OrderStatus oldStatus, OrderStatus newStatus) {
        for (OrderObserver observer : new ArrayList<>(observers)) {
            observer.update(this, oldStatus, newStatus);
        }
    }

    static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
