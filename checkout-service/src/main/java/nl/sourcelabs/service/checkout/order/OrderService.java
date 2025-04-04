package nl.sourcelabs.service.checkout.order;

import nl.sourcelabs.service.checkout.product.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    public void handleProductUpdate(Product product) {
        orders.values().stream()
            .filter(order -> order.orderItems().stream().anyMatch(item -> Objects.equals(item.product().id(), product.id())))
            .forEach(order -> orders.replace(order.id(), updateOrderItemProduct(order, product)));
    }

    private Order updateOrderItemProduct(Order order, Product product) {
        List<OrderItem> items = order.orderItems().stream()
            .map(it -> updateOrderItemProduct(it, product))
            .toList();

        return new Order(order.id(), order.customerNumber(), items);
    }

    private OrderItem updateOrderItemProduct(OrderItem orderItem, Product product) {
        if (orderItem.product().id().equals(product.id())) {
            return new OrderItem(product, orderItem.quantity());
        } else {
            return orderItem;
        }
    }
}
