package restaurant.factory;

import restaurant.model.MenuItem;

public abstract class Meal implements MenuItem {
    private final String name;
    private final String description;
    private final double price;

    protected Meal(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
