# Restaurant Ordering System

**Team:** Bekarys Zharylgassyn, Alisher Ospanov

## Patterns

| # | Pattern | Where | Author |
|---|---------|-------|--------|
| 1 | **Factory** | `restaurant.factory.MealFactory` creates Pizza, Burger, Pasta, Salad, Drink | Bekarys Zharylgassyn |
| 2 | **Decorator** | `restaurant.decorator.*` adds extras (cheese, bacon, mushrooms, sauce) to any meal | Bekarys Zharylgassyn |
| 3 | **Builder** | `restaurant.order.OrderBuilder` assembles and validates an `Order` | Bekarys Zharylgassyn |
| 4 | **Strategy** | `restaurant.strategy.*` interchangeable discount rules | Alisher Ospanov |
| 5 | **Facade** | `restaurant.facade.RestaurantFacade` single entry point for the whole system | Alisher Ospanov |
| 6 | **Observer** | `restaurant.observer.*` kitchen display and customer notifications on status change | Alisher Ospanov |

## Requirements

- We use Java 17+ and Maven 3.8+

## Build, test, run

```bash
mvn test
mvn package
java -jar target/restaurant-ordering-system-1.0.0.jar
```

## Order flow

`NEW -> PREPARING -> READY -> DELIVERED` (an order can also be `CANCELLED` before delivery).
