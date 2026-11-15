package restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import restaurant.decorator.Extra;
import restaurant.factory.MealFactory;
import restaurant.factory.MealType;
import restaurant.model.MenuItem;
import restaurant.model.OrderStatus;
import restaurant.order.Order;
import restaurant.order.OrderBuilder;

class BuilderTest {
    private static MenuItem pizzaWithCheese() {
        return Extra.CHEESE.apply(MealFactory.create(MealType.PIZZA));
    }

    @Test
    void buildsDineInOrder() {
        Order order = new OrderBuilder().customer("Alice").table(5).note("no onions")
                .addItem(pizzaWithCheese()).addItem(MealFactory.create(MealType.DRINK)).build();
        assertEquals("Alice", order.getCustomerName());
        assertEquals(5, order.getTableNumber());
        assertFalse(order.isTakeaway());
        assertEquals("no onions", order.getNote());
        assertEquals(2, order.getItems().size());
        assertEquals(12.50, order.getSubtotal(), 0.001);
        assertEquals(OrderStatus.NEW, order.getStatus());
    }

    @Test
    void buildsTakeawayOrderWithoutTable() {
        Order order = new OrderBuilder().customer("Bob").takeaway(true)
                .addItem(MealFactory.create(MealType.BURGER)).build();
        assertTrue(order.isTakeaway());
        assertEquals(0, order.getTableNumber());
    }

    @Test
    void validatesRequiredFields() {
        MenuItem drink = MealFactory.create(MealType.DRINK);
        assertThrows(IllegalStateException.class, () -> new OrderBuilder().table(1).addItem(drink).build());
        assertThrows(IllegalStateException.class, () -> new OrderBuilder().customer("A").table(1).build());
        assertThrows(IllegalStateException.class, () -> new OrderBuilder().customer("A").addItem(drink).build());
        assertThrows(IllegalArgumentException.class, () -> new OrderBuilder().addItem(null));
    }

    @Test
    void orderIdsAreUnique() {
        MenuItem drink = MealFactory.create(MealType.DRINK);
        Order a = new OrderBuilder().customer("A").table(1).addItem(drink).build();
        Order b = new OrderBuilder().customer("B").table(2).addItem(drink).build();
        assertNotEquals(a.getId(), b.getId());
    }

    @Test
    void itemsListIsImmutable() {
        Order order = new OrderBuilder().customer("A").table(1)
                .addItem(MealFactory.create(MealType.DRINK)).build();
        assertThrows(UnsupportedOperationException.class, () -> order.getItems().clear());
    }

    @Test
    void statusFollowsNormalFlowAndCannotChangeAfterEnd() {
        Order order = new OrderBuilder().customer("A").table(1)
                .addItem(MealFactory.create(MealType.DRINK)).build();
        order.advance();
        assertEquals(OrderStatus.PREPARING, order.getStatus());
        order.advance();
        order.advance();
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertThrows(IllegalStateException.class, order::advance);
        assertThrows(IllegalStateException.class, order::cancel);
    }
}
