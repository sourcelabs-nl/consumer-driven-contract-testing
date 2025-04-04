package nl.sourcelabs.service.checkout.product;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.MessagePact;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.sourcelabs.service.checkout.order.OrderService;
import org.apache.groovy.util.Maps;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

// https://github.com/pactflow/example-consumer-java-kafka/blob/master/src/test/java/io/pactflow/example/kafka/ProductsPactTest.java
@PactConsumerTest
@PactTestFor(providerName = "product-service")
class ProductServicePactConsumerTest {

    private ObjectMapper mapper = new ObjectMapper();


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
            .toPact().asV4Pact().get();
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


    @Pact(consumer = "checkout-service")
    public V4Pact productUpdateMessage(MessagePactBuilder builder) {
        return builder
            .expectsToReceive("a product update")
            .withContent("""
                    {
                        "id" : "1",
                        "title": "Apple - MacBook Pro - 16'",
                        "priceCents" : 3299000
                    }
                """)
            .withMetadata(Maps.of(
                "Content-Type", "application/json",
                "kafka_topic", "products"
            ))
            .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "productUpdateMessage", providerType = ProviderType.ASYNCH)
    public void testProductUpdate(MessagePact messagePact) throws Exception {
        var underTest = new ProductUpdateMessageListener(Mockito.mock(OrderService.class));
        Product product = mapper.readValue(messagePact.getMessages().getFirst().contentsAsString(), Product.class);

        assertDoesNotThrow(() -> {
            underTest.listen(product);
        });
    }
}