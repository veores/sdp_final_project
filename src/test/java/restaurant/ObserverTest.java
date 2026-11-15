package restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import restaurant.facade.ItemRequest;
import restaurant.facade.RestaurantFacade;
import restaurant.factory.MealFactory;
import restaurant.factory.MealType;
import restaurant.model.OrderStatus;
import restaurant.observer.OrderObserver;
import restaurant.order.Order;
import restaurant.order.OrderBuilder;

class ObserverTest {
    @Test
    void kitchenAndCustomerAreNotifiedAboutNewOrder() {
        RestaurantFacade restaurant = new RestaurantFacade();
        restaurant.placeOrder("Alice", 4, null, ItemRequest.of(MealType.PASTA));
        assertEquals(1, restaurant.getKitchenDisplay().getMessages().size());
        assertTrue(restaurant.getKitchenDisplay().getMessages().get(0).contains("New order"));
        assertTrue(restaurant.getCustomerNotifier().getMessages().get(0).contains("Alice"));
    }

    @Test
    void statusChangesReachBothObservers() {
        RestaurantFacade restaurant = new RestaurantFacade();
        Order order = restaurant.placeOrder("Alice", 4, null, ItemRequest.of(MealType.PASTA));
        restaurant.advance(order.getId());
        restaurant.advance(order.getId());
        assertTrue(restaurant.getKitchenDisplay().getMessages().get(1).contains("being cooked"));
        assertTrue(restaurant.getCustomerNotifier().getMessages().get(2).contains("ready"));
    }

    @Test
    void cancellationIsAnnounced() {
        RestaurantFacade restaurant = new RestaurantFacade();
        Order order = restaurant.placeOrder("Bob", 1, null, ItemRequest.of(MealType.BURGER));
        restaurant.cancel(order.getId());
        List<String> kitchen = restaurant.getKitchenDisplay().getMessages();
        assertTrue(kitchen.get(kitchen.size() - 1).contains("cancelled"));
    }

    @Test
    void customObserverCanBeAttachedAndDetached() {
        Order order = new OrderBuilder().customer("A").table(1)
                .addItem(MealFactory.create(MealType.DRINK)).build();
        List<OrderStatus> seen = new ArrayList<>();
        OrderObserver spy = (o, oldStatus, newStatus) -> seen.add(newStatus);
        order.attach(spy);
        order.advance();
        order.detach(spy);
        order.advance();
        assertEquals(List.of(OrderStatus.PREPARING), seen);
    }

    @Test
    void sameObserverIsNotAttachedTwice() {
        Order order = new OrderBuilder().customer("A").table(1)
                .addItem(MealFactory.create(MealType.DRINK)).build();
        List<OrderStatus> seen = new ArrayList<>();
        OrderObserver spy = (o, oldStatus, newStatus) -> seen.add(newStatus);
        order.attach(spy);
        order.attach(spy);
        order.advance();
        assertEquals(1, seen.size());
    }
}
