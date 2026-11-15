package restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import restaurant.decorator.Bacon;
import restaurant.decorator.Extra;
import restaurant.decorator.ExtraCheese;
import restaurant.factory.MealFactory;
import restaurant.factory.MealType;
import restaurant.model.MenuItem;

class DecoratorTest {
    @Test
    void singleExtraAddsPriceAndDescription() {
        MenuItem pizza = new ExtraCheese(MealFactory.create(MealType.PIZZA));
        assertEquals(10.00, pizza.getPrice(), 0.001);
        assertTrue(pizza.getDescription().endsWith("+ extra cheese"));
        assertEquals("Margherita Pizza", pizza.getName());
    }

    @Test
    void decoratorsCanBeStacked() {
        MenuItem pizza = new Bacon(new ExtraCheese(MealFactory.create(MealType.PIZZA)));
        assertEquals(12.00, pizza.getPrice(), 0.001);
        assertTrue(pizza.getDescription().contains("extra cheese + bacon"));
    }

    @Test
    void sameExtraCanBeAddedTwice() {
        MenuItem item = Extra.CHEESE.apply(Extra.CHEESE.apply(MealFactory.create(MealType.BURGER)));
        assertEquals(10.50, item.getPrice(), 0.001);
    }

    @Test
    void extraEnumAppliesEveryExtra() {
        for (Extra extra : Extra.values()) {
            MenuItem decorated = extra.apply(MealFactory.create(MealType.PASTA));
            assertTrue(decorated.getPrice() > 9.00);
        }
    }

    @Test
    void rejectsNullItem() {
        assertThrows(IllegalArgumentException.class, () -> new Bacon(null));
    }
}
