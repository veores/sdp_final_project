package restaurant.strategy;

import java.util.Locale;

public class FixedAmountDiscount implements DiscountStrategy {
    private final String name;
    private final double amount;

    public FixedAmountDiscount(String name, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }
        this.name = name;
        this.amount = amount;
    }

    @Override
    public double apply(double subtotal) {
        return Math.max(0, subtotal - amount);
    }

    @Override
    public String getName() {
        return String.format(Locale.US, "%s -$%.2f", name, amount);
    }
}
