package restaurant.decorator;

import restaurant.model.MenuItem;

public abstract class MealDecorator implements MenuItem {
    protected final MenuItem wrapped;

    protected MealDecorator(MenuItem wrapped) {
        if (wrapped == null) {
            throw new IllegalArgumentException("Decorated item must not be null");
        }
        this.wrapped = wrapped;
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " + " + extraLabel();
    }

    @Override
    public double getPrice() {
        return wrapped.getPrice() + extraPrice();
    }

    protected abstract String extraLabel();

    protected abstract double extraPrice();
}
