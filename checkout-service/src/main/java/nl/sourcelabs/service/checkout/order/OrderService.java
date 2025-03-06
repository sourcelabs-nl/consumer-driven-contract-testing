package nl.sourcelabs.service.checkout.order;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public Order addItemToOrder(String orderId, OrderItem orderItem) {
        var order = orders.get(orderId);
        order.orderItems().add(orderItem);
        return order;
    }

    public Order getOrderById(String invoiceId) {
        return orders.get(invoiceId);
    }

    public Order createOrder(String customerNumber) {
        var id = UUID.randomUUID().toString();
        var order = new Order(id, customerNumber, new ArrayList<>());
        orders.put(id, order);
        return order;
    }

    public List<Order> getOrdersByCustomer(String customerNumber) {
        return orders.values().stream()
            .filter(it -> Objects.equals(customerNumber, it.customerNumber()) )
            .toList();
    }
}
