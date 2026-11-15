package restaurant.decorator;

import restaurant.model.MenuItem;

public class SpicySauce extends MealDecorator {
    public SpicySauce(MenuItem wrapped) {
        super(wrapped);
    }

    @Override
    protected String extraLabel() {
        return "spicy sauce";
    }

    @Override
    protected double extraPrice() {
        return 0.50;
    }
}
