package restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import restaurant.decorator.Extra;
import restaurant.facade.ItemRequest;
import restaurant.facade.RestaurantFacade;
import restaurant.factory.MealType;
import restaurant.model.OrderStatus;
import restaurant.order.Order;
import restaurant.strategy.PercentageDiscount;

class FacadeTest {
    @Test
    void placesOrderUsingAllSubsystems() {
        RestaurantFacade restaurant = new RestaurantFacade();
        Order order = restaurant.placeOrder("Alice", 3, new PercentageDiscount("Student", 10),
                ItemRequest.of(MealType.PIZZA, Extra.CHEESE), ItemRequest.of(MealType.DRINK));
        assertEquals(12.50, order.getSubtotal(), 0.001);
        assertEquals(11.25, order.getTotal(), 0.001);
        assertEquals(OrderStatus.NEW, order.getStatus());
    }

    @Test
    void nullDiscountMeansNoDiscount() {
        RestaurantFacade restaurant = new RestaurantFacade();
        Order order = restaurant.placeTakeaway("Bob", null, ItemRequest.of(MealType.BURGER));
        assertTrue(order.isTakeaway());
        assertEquals(7.50, order.getTotal(), 0.001);
    }

    @Test
    void advanceAndCancelWorkById() {
        RestaurantFacade restaurant = new RestaurantFacade();
        Order order = restaurant.placeOrder("A", 1, null, ItemRequest.of(MealType.SALAD));
        restaurant.advance(order.getId());
        assertEquals(OrderStatus.PREPARING, restaurant.getOrder(order.getId()).getStatus());
        restaurant.cancel(order.getId());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void receiptContainsKeyInformation() {
        RestaurantFacade restaurant = new RestaurantFacade();
        Order order = restaurant.placeOrder("Alice", 3, null, ItemRequest.of(MealType.PIZZA, Extra.BACON));
        String receipt = restaurant.receipt(order.getId());
        assertTrue(receipt.contains("#" + order.getId()));
        assertTrue(receipt.contains("Alice"));
        assertTrue(receipt.contains("Margherita Pizza"));
        assertTrue(receipt.contains("TOTAL"));
    }

    @Test
    void unknownOrderIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new RestaurantFacade().getOrder(42));
    }
}
