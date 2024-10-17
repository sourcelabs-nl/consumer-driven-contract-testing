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
class CustomerServiceClientPactTest {

    @Pact(consumer = "invoice-service")
    public V4Pact getCustomerById(PactDslWithProvider builder) {
        return null;
    }

    @Test
    @PactTestFor(pactMethod = "getCustomerById")
    public void testGetCustomerById(MockServer mockServer) {
        var underTest = new CustomerServiceClient(mockServer.getUrl());
        Customer customer = underTest.getCustomerById("cust123");

        assertEquals("cust123", customer.customerId());
        assertEquals("1234AB", customer.invoiceAddress().postalCode());
        assertEquals("123", customer.invoiceAddress().houseNumber());
    }
}