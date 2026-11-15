package restaurant.facade;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import restaurant.decorator.Extra;
import restaurant.factory.MealFactory;
import restaurant.model.MenuItem;
import restaurant.order.Order;
import restaurant.order.OrderBuilder;
import restaurant.strategy.DiscountStrategy;
import restaurant.strategy.NoDiscount;

public class RestaurantFacade {
    private final Map<Integer, Order> orders = new LinkedHashMap<>();

    public Order placeOrder(String customer, int table, DiscountStrategy discount, ItemRequest... requests) {
        return create(customer, table, false, discount, requests);
    }

    public Order placeTakeaway(String customer, DiscountStrategy discount, ItemRequest... requests) {
        return create(customer, 0, true, discount, requests);
    }

    public void advance(int orderId) {
        getOrder(orderId).advance();
    }

    public void cancel(int orderId) {
        getOrder(orderId).cancel();
    }

    public Order getOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Unknown order #" + orderId);
        }
        return order;
    }

    public String receipt(int orderId) {
        Order order = getOrder(orderId);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "===== RECEIPT - Order #%d =====%n", order.getId()));
        sb.append(String.format(Locale.US, "Customer: %s | %s | Status: %s%n", order.getCustomerName(),
                order.isTakeaway() ? "Takeaway" : "Table " + order.getTableNumber(), order.getStatus()));
        for (MenuItem item : order.getItems()) {
            sb.append(String.format(Locale.US, "  %-26s $%6.2f%n", item.getName(), item.getPrice()));
            sb.append(String.format(Locale.US, "    (%s)%n", item.getDescription()));
        }
        sb.append(String.format(Locale.US, "Subtotal:                    $%6.2f%n", order.getSubtotal()));
        sb.append(String.format(Locale.US, "Discount [%s]: -$%.2f%n",
                order.getDiscountStrategy().getName(), order.getDiscountAmount()));
        sb.append(String.format(Locale.US, "TOTAL:                       $%6.2f%n", order.getTotal()));
        return sb.toString();
    }

    private Order create(String customer, int table, boolean takeaway,
                         DiscountStrategy discount, ItemRequest[] requests) {
        OrderBuilder builder = new OrderBuilder().customer(customer).table(table).takeaway(takeaway);
        for (ItemRequest request : requests) {
            MenuItem item = MealFactory.create(request.getType());
            for (Extra extra : request.getExtras()) {
                item = extra.apply(item);
            }
            builder.addItem(item);
        }
        Order order = builder.build();
        order.setDiscountStrategy(discount == null ? new NoDiscount() : discount);
        orders.put(order.getId(), order);
        return order;
    }
}
