package restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import restaurant.factory.MealFactory;
import restaurant.factory.MealType;
import restaurant.order.Order;
import restaurant.order.OrderBuilder;
import restaurant.strategy.FixedAmountDiscount;
import restaurant.strategy.HappyHourDiscount;
import restaurant.strategy.NoDiscount;
import restaurant.strategy.PercentageDiscount;

class StrategyTest {
    private static Clock clockAt(String isoInstant) {
        return Clock.fixed(Instant.parse(isoInstant), ZoneOffset.UTC);
    }

    @Test
    void noDiscountKeepsPrice() {
        assertEquals(20.0, new NoDiscount().apply(20.0), 0.001);
    }

    @Test
    void percentageDiscount() {
        assertEquals(18.0, new PercentageDiscount("Student", 10).apply(20.0), 0.001);
        assertThrows(IllegalArgumentException.class, () -> new PercentageDiscount("Bad", 120));
    }

    @Test
    void fixedAmountNeverGoesBelowZero() {
        assertEquals(15.0, new FixedAmountDiscount("Coupon", 5).apply(20.0), 0.001);
        assertEquals(0.0, new FixedAmountDiscount("Coupon", 5).apply(3.0), 0.001);
    }

    @Test
    void happyHourOnlyWorksInsideItsWindow() {
        HappyHourDiscount inside = new HappyHourDiscount(16, 18, 20, clockAt("2026-11-14T17:00:00Z"));
        HappyHourDiscount outside = new HappyHourDiscount(16, 18, 20, clockAt("2026-11-14T12:00:00Z"));
        assertEquals(16.0, inside.apply(20.0), 0.001);
        assertEquals(20.0, outside.apply(20.0), 0.001);
    }

    @Test
    void orderUsesItsStrategyAndCanSwitchIt() {
        Order order = new OrderBuilder().customer("A").table(1)
                .addItem(MealFactory.create(MealType.PASTA)).addItem(MealFactory.create(MealType.SALAD)).build();
        assertEquals(15.0, order.getTotal(), 0.001);
        order.setDiscountStrategy(new PercentageDiscount("Loyalty", 20));
        assertEquals(12.0, order.getTotal(), 0.001);
        assertEquals(3.0, order.getDiscountAmount(), 0.001);
        order.setDiscountStrategy(new FixedAmountDiscount("Coupon", 5));
        assertEquals(10.0, order.getTotal(), 0.001);
        assertThrows(IllegalArgumentException.class, () -> order.setDiscountStrategy(null));
    }
}
