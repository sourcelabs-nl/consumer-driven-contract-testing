package nl.sourcelabs.service.invoice;

import nl.sourcelabs.service.invoice.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CustomerServiceClient {
    private final RestClient restClient;

    public CustomerServiceClient(@Value("${customer.service.base-url}") String baseUrl) {
        restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public Customer getCustomerById(String customerId) {
        var response = restClient.get()
            .uri("/customers/{customer_id}", customerId)
            .header("Accept", "application/json")
            .retrieve()
            .toEntity(Customer.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return null;
    }
}