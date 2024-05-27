package nl.sourcelabs.service.invoice;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import nl.sourcelabs.service.invoice.model.Customer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PactConsumerTest
@PactTestFor(providerName = "customer-service")
class CustomerServicePactTest {

    @Pact(consumer = "invoice-service")
    public V4Pact productById(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return builder
            .given("customer with customerId: cust123 exists")
            .uponReceiving("get customer by id")
            .path("/customers/cust123")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(headers)
            .body("""
                {
                    "customerId": "cust123",
                    "invoiceAddress" : {
                        "postalCode" : "1234AB",
                        "houseNumber" : "123"
                    }
                }""")
            .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "productById")
    public void testGetCustomerById(MockServer mockServer) {
        var underTest = new CustomerService(mockServer.getUrl());
        Customer customer = underTest.getCustomerById("cust123");

        assertEquals("cust123", customer.customerId());
        assertEquals("1234AB", customer.invoiceAddress().postalCode());
        assertEquals("123", customer.invoiceAddress().houseNumber());
    }
}