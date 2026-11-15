package restaurant.decorator;

import restaurant.model.MenuItem;

public class Bacon extends MealDecorator {
    public Bacon(MenuItem wrapped) {
        super(wrapped);
    }

    @Override
    protected String extraLabel() {
        return "bacon";
    }

    @Override
    protected double extraPrice() {
        return 2.00;
    }
}
