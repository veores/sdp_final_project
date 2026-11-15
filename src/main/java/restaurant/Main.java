package restaurant;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import restaurant.decorator.Extra;
import restaurant.facade.ItemRequest;
import restaurant.facade.RestaurantFacade;
import restaurant.factory.MealType;
import restaurant.order.Order;
import restaurant.strategy.FixedAmountDiscount;
import restaurant.strategy.HappyHourDiscount;
import restaurant.strategy.PercentageDiscount;

public class Main {
    public static void main(String[] args) {
        RestaurantFacade restaurant = new RestaurantFacade();

        System.out.println("##### Order 1: dine-in, student discount (Strategy) #####");
        Order o1 = restaurant.placeOrder("Alice", 5, new PercentageDiscount("Student", 10),
                ItemRequest.of(MealType.PIZZA, Extra.CHEESE, Extra.BACON),
                ItemRequest.of(MealType.DRINK));
        System.out.println(restaurant.receipt(o1.getId()));
        for (int i = 0; i < 3; i++) {
            restaurant.advance(o1.getId());
        }
        System.out.println("Final status of order #" + o1.getId() + ": " + o1.getStatus());

        System.out.println();
        System.out.println("##### Order 2: takeaway, happy hour (fixed clock 17:00), then cancelled #####");
        Clock fixedClock = Clock.fixed(Instant.parse("2026-11-14T17:00:00Z"), ZoneOffset.UTC);
        Order o2 = restaurant.placeTakeaway("Bob", new HappyHourDiscount(16, 18, 20, fixedClock),
                ItemRequest.of(MealType.BURGER, Extra.SPICY_SAUCE, Extra.MUSHROOMS),
                ItemRequest.of(MealType.PASTA));
        System.out.println(restaurant.receipt(o2.getId()));
        restaurant.advance(o2.getId());
        restaurant.cancel(o2.getId());
        System.out.println("Final status of order #" + o2.getId() + ": " + o2.getStatus());

        System.out.println();
        System.out.println("##### Order 3: dine-in, coupon -$5 #####");
        Order o3 = restaurant.placeOrder("Carol", 2, new FixedAmountDiscount("Coupon", 5),
                ItemRequest.of(MealType.SALAD),
                ItemRequest.of(MealType.PASTA, Extra.MUSHROOMS));
        System.out.println(restaurant.receipt(o3.getId()));
        restaurant.advance(o3.getId());
        System.out.println("Final status of order #" + o3.getId() + ": " + o3.getStatus());
    }
}
