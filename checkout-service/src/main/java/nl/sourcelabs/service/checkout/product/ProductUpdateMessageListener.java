package nl.sourcelabs.service.checkout.product;

import nl.sourcelabs.service.checkout.order.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductUpdateMessageListener {

    private final OrderService orderService;
    private final static Logger LOGGER = LogManager.getLogger();

    public ProductUpdateMessageListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "products")
    public void listen(Product product) {
        LOGGER.info("received product update event: {}", product);
        orderService.handleProductUpdate(product);
    }
}
