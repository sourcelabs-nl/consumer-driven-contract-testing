package nl.sourcelabs.service.checkout.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ProductServiceClient {
    private final RestClient restClient;

    public ProductServiceClient(@Value("${customer.service.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public Product getProductById(String productId) {
        return restClient.get()
            .uri("/products/{id}", productId)
            .header("Accept", "application/json")
            .retrieve()
            .onStatus(status -> status == NOT_FOUND, (request, response) -> {
                throw new ProductNotFoundException();
            })
            .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                throw new ProductServiceException();
            })
            .toEntity(Product.class)
            .getBody();
    }
}