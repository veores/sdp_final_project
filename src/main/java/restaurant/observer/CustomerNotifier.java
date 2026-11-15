package restaurant.observer;

import java.util.Locale;
import restaurant.model.OrderStatus;
import restaurant.order.Order;

public class CustomerNotifier extends LoggingObserver {
    @Override
    public void update(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        String prefix = "[CUSTOMER -> " + order.getCustomerName() + "] ";
        switch (newStatus) {
            case NEW -> publish(prefix + String.format(Locale.US,
                    "Order #%d received. Total: $%.2f", order.getId(), order.getTotal()));
            case PREPARING -> publish(prefix + "Your order is being prepared");
            case READY -> publish(prefix + "Your order is ready!");
            case DELIVERED -> publish(prefix + "Enjoy your meal, " + order.getCustomerName() + "!");
            case CANCELLED -> publish(prefix + "Your order was cancelled");
        }
    }
}
