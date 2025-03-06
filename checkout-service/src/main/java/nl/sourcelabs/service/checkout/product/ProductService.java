package nl.sourcelabs.service.checkout.product;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductServiceClient productServiceClient;

    public ProductService(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    public Product getProductById(String productId) {
        return productServiceClient.getProductById(productId);
    }
}
