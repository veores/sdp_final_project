package restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import restaurant.factory.MealFactory;
import restaurant.factory.MealType;
import restaurant.factory.Pizza;
import restaurant.model.MenuItem;

class FactoryTest {
    @Test
    void createsPizza() {
        MenuItem item = MealFactory.create(MealType.PIZZA);
        assertTrue(item instanceof Pizza);
        assertEquals("Margherita Pizza", item.getName());
        assertEquals(8.50, item.getPrice(), 0.001);
    }

    @Test
    void createsEveryMealType() {
        for (MealType type : MealType.values()) {
            MenuItem item = MealFactory.create(type);
            assertTrue(item.getPrice() > 0, type + " must have a price");
        }
    }

    @Test
    void createsByNameIgnoringCase() {
        assertEquals("Beef Burger", MealFactory.create(" burger ").getName());
    }

    @Test
    void rejectsNullAndUnknownTypes() {
        assertThrows(IllegalArgumentException.class, () -> MealFactory.create((MealType) null));
        assertThrows(IllegalArgumentException.class, () -> MealFactory.create(" "));
        assertThrows(IllegalArgumentException.class, () -> MealFactory.create("sushi"));
    }
}
