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

## Interactive Console Application

**Implemented by: Alisher Ospanov**

An additional interactive console application is available in `restaurant.console.ConsoleApp`. It allows users to interact with the restaurant ordering system dynamically through the command line

Features:
- Create dine-in and takeaway orders
- Select meals and additional ingredients
- Choose discount strategies
- View all created orders
- Advance order statuses or cancel orders
- Generate and view order receipts
- View kitchen and customer notifications

The console application uses the existing `RestaurantFacade` and design pattern implementations. It provides an additional way to demonstrate the system without modifying the original demo application or existing tests

## Requirements

- We use Java 17+ and Maven 3.8+

## Build, test, run

```bash
mvn test
mvn package
java -jar target/restaurant-ordering-system-1.0.0.jar
```

To run the interactive console application from the project root:

```bash
java -cp target/classes restaurant.console.ConsoleApp
```

## Order flow

`NEW -> PREPARING -> READY -> DELIVERED` (an order can also be `CANCELLED` before delivery).