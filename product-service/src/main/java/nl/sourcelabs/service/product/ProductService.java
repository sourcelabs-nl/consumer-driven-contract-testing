package nl.sourcelabs.service.product;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductService {

    private Map<String, Product> products = Map.of(
        "1", new Product("1", "Apple - MacBook Pro - 16'", 3499_000),
        "2", new Product("2", "Apple - MacBook Air - 13'", 1299_000)
    );

    public Product getProductById(String productId) {
        return products.get(productId);
    }
}
