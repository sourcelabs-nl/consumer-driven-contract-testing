package nl.sourcelabs.service.invoice;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import nl.sourcelabs.service.invoice.model.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest // https://wiremock.org/docs/junit-jupiter/
class CustomerServiceTest {

    @Test
    void testGetCustomerById(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMock.stubFor(
            WireMock.get("/customers/cust123")
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                            {
                              "customerId": "cust123",
                              "invoiceAddress" : {
                                "postalCode" : "1234AB",
                                "houseNumber" : "123"
                              }
                            }
                        """))
        );

        var underTest = new CustomerService(wmRuntimeInfo.getHttpBaseUrl());
        Customer customer = underTest.getCustomerById("cust123");

        assertEquals("cust123", customer.customerId());
        assertEquals("1234AB", customer.invoiceAddress().postalCode());
        assertEquals("123", customer.invoiceAddress().houseNumber());
    }

    @Test
    void testGetCustomerByIdWithWireMockCloud() {
        var underTest = new CustomerService("https://qgdoz.wiremockapi.cloud");
        Customer customer = underTest.getCustomerById("cust123");

        assertEquals("cust123", customer.customerId());
        assertEquals("1234AB", customer.invoiceAddress().postalCode());
        assertEquals("123", customer.invoiceAddress().houseNumber());
    }
}