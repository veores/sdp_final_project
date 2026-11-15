package restaurant.observer;

import restaurant.model.OrderStatus;
import restaurant.order.Order;

public interface OrderObserver {
    void update(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}
