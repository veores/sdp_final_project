package restaurant.decorator;

import restaurant.model.MenuItem;

public class Mushrooms extends MealDecorator {
    public Mushrooms(MenuItem wrapped) {
        super(wrapped);
    }

    @Override
    protected String extraLabel() {
        return "mushrooms";
    }

    @Override
    protected double extraPrice() {
        return 1.00;
    }
}
