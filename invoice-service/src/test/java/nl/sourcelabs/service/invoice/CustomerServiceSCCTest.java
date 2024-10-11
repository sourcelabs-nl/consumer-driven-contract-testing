package nl.sourcelabs.service.invoice;

import nl.sourcelabs.service.invoice.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
//@AutoConfigureStubRunner(
//    stubsMode = StubRunnerProperties.StubsMode.LOCAL,
//    ids = {"nl.sourcelabs:customer-service:+:stubs:9999"}
//)
class CustomerServiceSCCTest {

   // @Test
    void testGetCustomerByIdWithSpringCloudContract() {
        var underTest = new CustomerService("http://localhost:9999");
        Customer customer = underTest.getCustomerById("cust123");

        assertEquals("cust123", customer.customerId());
        assertEquals("1234AB", customer.invoiceAddress().postalCode());
        assertEquals("123", customer.invoiceAddress().houseNumber());
    }
}
