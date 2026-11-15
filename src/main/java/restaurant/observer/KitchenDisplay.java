package restaurant.observer;

import java.util.ArrayList;
import java.util.List;
import restaurant.model.MenuItem;
import restaurant.model.OrderStatus;
import restaurant.order.Order;

public class KitchenDisplay extends LoggingObserver {
    @Override
    public void update(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        String id = "#" + order.getId();
        switch (newStatus) {
            case NEW -> publish("[KITCHEN] New order " + id + " (" + where(order) + "): " + summary(order));
            case PREPARING -> publish("[KITCHEN] Order " + id + " is being cooked");
            case READY -> publish("[KITCHEN] Order " + id + " is ready for service");
            case CANCELLED -> publish("[KITCHEN] Order " + id + " cancelled - stop cooking");
            case DELIVERED -> { }
        }
    }

    private static String where(Order order) {
        return order.isTakeaway() ? "takeaway" : "table " + order.getTableNumber();
    }

    private static String summary(Order order) {
        List<String> parts = new ArrayList<>();
        for (MenuItem item : order.getItems()) {
            parts.add(item.getName() + " [" + item.getDescription() + "]");
        }
        return String.join("; ", parts);
    }
}
