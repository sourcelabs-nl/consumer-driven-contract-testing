package nl.sourcelabs.service.customer;


import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import nl.sourcelabs.service.customer.model.Customer;
import nl.sourcelabs.service.customer.model.InvoiceAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.mockito.Mockito.when;


@Provider("customer-service")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PactBroker
public class CustomerServiceVerifierPact {

    @LocalServerPort
    private int port;

    @MockBean
    CustomerService customerService;

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port, "/"));
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("customer with customerId: cust123 exists")
    public void customerWithCustomerIdCust123Exists() {
        when(customerService.getCustomerById("cust123"))
            .thenReturn(new Customer("cust123", new InvoiceAddress("1234AB", "123")));
    }

}

