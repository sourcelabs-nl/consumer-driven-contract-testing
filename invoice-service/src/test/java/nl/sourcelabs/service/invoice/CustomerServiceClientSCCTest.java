package nl.sourcelabs.service.invoice;

import nl.sourcelabs.service.invoice.model.Customer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureStubRunner(
    stubsMode = StubRunnerProperties.StubsMode.LOCAL,
    ids = {"nl.sourcelabs:customer-service:+:stubs"}
)
class CustomerServiceClientSCCTest {

    @StubRunnerPort("nl.sourcelabs:customer-service")
    private int stubRunnerPort;

    @Test
    @Disabled
    void testGetCustomerById() {
        var underTest = new CustomerServiceClient("http://localhost:"+ stubRunnerPort);
        Customer customer = underTest.getCustomerById("cust123");

        assertEquals("cust123", customer.customerId());
        assertEquals("1234AB", customer.invoiceAddress().postalCode());
        assertEquals("123", customer.invoiceAddress().houseNumber());
    }


    @Test
    @Disabled
    void testGetCustomerByIdWithManualStubRunner() {
        var underTest = new CustomerServiceClient("http://localhost:9090");
        Customer customer = underTest.getCustomerById("cust123");

        assertEquals("cust123", customer.customerId());
        assertEquals("1234AB", customer.invoiceAddress().postalCode());
        assertEquals("123", customer.invoiceAddress().houseNumber());
    }
}
