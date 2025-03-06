package nl.sourcelabs.service.checkout.order;

import org.junit.jupiter.api.BeforeEach;

public class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService();
    }
}
