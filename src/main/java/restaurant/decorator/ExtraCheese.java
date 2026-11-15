package restaurant.decorator;

import restaurant.model.MenuItem;

public class ExtraCheese extends MealDecorator {
    public ExtraCheese(MenuItem wrapped) {
        super(wrapped);
    }

    @Override
    protected String extraLabel() {
        return "extra cheese";
    }

    @Override
    protected double extraPrice() {
        return 1.50;
    }
}
