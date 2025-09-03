package nl.sourcelabs.service.invoice;

import nl.sourcelabs.service.invoice.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureStubRunner(
    stubsMode = StubRunnerProperties.StubsMode.LOCAL,
    ids = "nl.sourcelabs:customer-service:+:stubs:8080"
)
class CustomerServiceContractTest {

    @Test
    void testGetCustomerByIdWithSpringCloudContract() {
        var underTest = new CustomerService("http://localhost:8080");
        Customer customer = underTest.getCustomerById("cust123");

        assertNotNull(customer);
        assertEquals("cust123", customer.customerId());
        assertNotNull(customer.invoiceAddress());
        assertEquals("1234AB", customer.invoiceAddress().zipCode());
        assertEquals("123", customer.invoiceAddress().houseNumber());
    }
}
