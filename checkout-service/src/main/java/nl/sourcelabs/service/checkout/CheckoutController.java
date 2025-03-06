package nl.sourcelabs.service.checkout;

import nl.sourcelabs.service.checkout.order.Order;
import nl.sourcelabs.service.checkout.order.OrderItem;
import nl.sourcelabs.service.checkout.order.OrderService;
import nl.sourcelabs.service.checkout.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class CheckoutController {

    private final OrderService orderService;
    private final ProductService productService;

    public CheckoutController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders(@RequestParam("customer-number") String customerNumber) {
        var orders = orderService.getOrdersByCustomer(customerNumber);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        var order = orderService.createOrder(request.customerNumber());
        return ResponseEntity.created(URI.create("/orders/" + order.id())).body(order);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") String id) {
        var order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/orders/{id}/items")
    public ResponseEntity<Order> addOrderItem(@PathVariable("id") String id, @RequestBody AddOrderItemRequest request) {
        var product = productService.getProductById(request.productId());
        var orderItem = new OrderItem(product, request.quantity());
        var order = orderService.addItemToOrder(id, orderItem);
        return ResponseEntity.ok(order);
    }
}
