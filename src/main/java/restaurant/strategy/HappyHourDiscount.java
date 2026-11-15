package restaurant.strategy;

import java.time.Clock;
import java.time.LocalTime;

public class HappyHourDiscount implements DiscountStrategy {
    private final int fromHour;
    private final int toHour;
    private final double percent;
    private final Clock clock;

    public HappyHourDiscount() {
        this(16, 18, 20, Clock.systemDefaultZone());
    }

    public HappyHourDiscount(int fromHour, int toHour, double percent, Clock clock) {
        this.fromHour = fromHour;
        this.toHour = toHour;
        this.percent = percent;
        this.clock = clock;
    }

    @Override
    public double apply(double subtotal) {
        int hour = LocalTime.now(clock).getHour();
        boolean active = hour >= fromHour && hour < toHour;
        return active ? subtotal * (1 - percent / 100.0) : subtotal;
    }

    @Override
    public String getName() {
        return "Happy hour " + fromHour + ":00-" + toHour + ":00 -" + (int) percent + "%";
    }
}
