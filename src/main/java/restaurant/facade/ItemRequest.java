package restaurant.facade;

import restaurant.decorator.Extra;
import restaurant.factory.MealType;

public final class ItemRequest {
    private final MealType type;
    private final Extra[] extras;

    public ItemRequest(MealType type, Extra... extras) {
        this.type = type;
        this.extras = extras.clone();
    }

    public static ItemRequest of(MealType type, Extra... extras) {
        return new ItemRequest(type, extras);
    }

    public MealType getType() {
        return type;
    }

    public Extra[] getExtras() {
        return extras.clone();
    }
}
