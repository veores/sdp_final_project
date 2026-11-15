package restaurant.strategy;

public class PercentageDiscount implements DiscountStrategy {
    private final String name;
    private final double percent;

    public PercentageDiscount(String name, double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent must be between 0 and 100");
        }
        this.name = name;
        this.percent = percent;
    }

    @Override
    public double apply(double subtotal) {
        return subtotal * (1 - percent / 100.0);
    }

    @Override
    public String getName() {
        return name + " -" + (percent == Math.floor(percent) ? String.valueOf((int) percent) : String.valueOf(percent)) + "%";
    }
}
