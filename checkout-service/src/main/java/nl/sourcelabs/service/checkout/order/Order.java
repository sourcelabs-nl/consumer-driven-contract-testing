package nl.sourcelabs.service.checkout.order;

import java.util.List;

public record Order(String id, String customerNumber, List<OrderItem> orderItems) {

    public Long getOrderPriceCents() {
        return orderItems().stream().mapToLong(i -> i.quantity() * i.product().priceCents()).sum();
    }
}
