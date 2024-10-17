package nl.sourcelabs.service.invoice;

import nl.sourcelabs.service.invoice.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@AutoConfigureStubRunner(
//    stubsMode = StubRunnerProperties.StubsMode.LOCAL
////    ids = {"nl.sourcelabs:customer-service:+:stubs"}
//)
class CustomerServiceClientSCCTest {

//    @StubRunnerPort("nl.sourcelabs:customer-service")
    private int stubRunnerPort;

    @Test
    void testGetCustomerById() {
        var underTest = new CustomerServiceClient("http://localhost:"+ stubRunnerPort);
        Customer customer = underTest.getCustomerById("cust123");

        assertEquals("cust123", customer.customerId());
        assertEquals("1234AB", customer.invoiceAddress().postalCode());
        assertEquals("123", customer.invoiceAddress().houseNumber());
    }
}
