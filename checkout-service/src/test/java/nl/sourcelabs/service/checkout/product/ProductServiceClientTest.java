package nl.sourcelabs.service.checkout.product;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// https://wiremock.org/docs/junit-jupiter/
// https://wiremock.org/docs/solutions/pact/
@WireMockTest
class ProductServiceClientTest {

    @BeforeEach
    void beforeEach() {
        WireMock.stubFor(
            WireMock.get("/products/1")
                .willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                            {
                                 "id" : "1",
                                 "title" : "Apple - MacBook Pro - 16'",
                                 "priceCents" : 3499000
                            }
                        """)
                )
        );
    }

    @Test
    void getProductByIdTest(WireMockRuntimeInfo wireMock) {
        var underTest = new ProductServiceClient(wireMock.getHttpBaseUrl());
        Product product = underTest.getProductById("1");

        assertThat(product.id()).isNotEmpty();
        assertThat(product.title()).isNotEmpty();
        assertThat(product.priceCents()).isGreaterThan(0);
    }
}