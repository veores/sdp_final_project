package restaurant.strategy;

public class NoDiscount implements DiscountStrategy {
    @Override
    public double apply(double subtotal) {
        return subtotal;
    }

    @Override
    public String getName() {
        return "No discount";
    }
}
