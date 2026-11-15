package restaurant.decorator;

import java.util.function.Function;
import restaurant.model.MenuItem;

public enum Extra {
    CHEESE(ExtraCheese::new),
    BACON(Bacon::new),
    MUSHROOMS(Mushrooms::new),
    SPICY_SAUCE(SpicySauce::new);

    private final Function<MenuItem, MenuItem> wrapper;

    Extra(Function<MenuItem, MenuItem> wrapper) {
        this.wrapper = wrapper;
    }

    public MenuItem apply(MenuItem item) {
        return wrapper.apply(item);
    }
}
