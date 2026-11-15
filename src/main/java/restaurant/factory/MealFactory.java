package restaurant.factory;

import restaurant.model.MenuItem;

public final class MealFactory {
    private MealFactory() {
    }

    public static MenuItem create(MealType type) {
        if (type == null) {
            throw new IllegalArgumentException("Meal type must not be null");
        }
        return switch (type) {
            case PIZZA -> new Pizza();
            case BURGER -> new Burger();
            case PASTA -> new Pasta();
            case SALAD -> new Salad();
            case DRINK -> new Drink();
        };
    }

    public static MenuItem create(String typeName) {
        if (typeName == null || typeName.isBlank()) {
            throw new IllegalArgumentException("Meal type name must not be blank");
        }
        return create(MealType.valueOf(typeName.trim().toUpperCase()));
    }
}
