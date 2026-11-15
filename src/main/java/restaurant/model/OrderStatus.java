package restaurant.model;

public enum OrderStatus {
    NEW, PREPARING, READY, DELIVERED, CANCELLED;

    public OrderStatus next() {
        return switch (this) {
            case NEW -> PREPARING;
            case PREPARING -> READY;
            case READY -> DELIVERED;
            case DELIVERED, CANCELLED -> throw new IllegalStateException("Order is already " + this);
        };
    }
}
