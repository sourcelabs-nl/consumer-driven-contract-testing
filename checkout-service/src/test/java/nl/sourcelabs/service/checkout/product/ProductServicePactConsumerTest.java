package nl.sourcelabs.service.checkout.product;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.apache.groovy.util.Maps;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@PactConsumerTest
@PactTestFor(providerName = "product-service")
class ProductServicePactConsumerTest {

    @Pact(consumer = "checkout-service")
    public V4Pact getProductById(PactDslWithProvider builder) {
        return builder
            .given("a product exists")
            .uponReceiving("get product by id")
            .path("/products/1")
            .method("GET")
            .headers(Maps.of("Accept", "application/json"))
            .willRespondWith()
            .status(200)
            .headers(Maps.of("Content-Type", "application/json"))
            .body("""
                    {
                        "id" : "1",
                        "title": "Apple - MacBook Pro - 16'",
                        "priceCents" : 3499000
                    }
                """)
            .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "getProductById")
    public void testGetCustomerById(MockServer mockServer) {
        var underTest = new ProductServiceClient(mockServer.getUrl());
        Product product = underTest.getProductById("1");

        assertThat(product.id()).isNotEmpty();
        assertThat(product.title()).isNotEmpty();
        assertThat(product.priceCents()).isGreaterThan(0);
    }
}