package nl.sourcelabs.service.customer;

import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import nl.sourcelabs.service.customer.model.Customer;
import nl.sourcelabs.service.customer.model.InvoiceAddress;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;

@SpringBootTest
public class BaseTestClass {

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        EncoderConfig encoderConfig = new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig().encoderConfig(encoderConfig);
        RestAssuredMockMvc.webAppContextSetup(this.webApplicationContext);

        setupMocksForVerifierTests();
    }

    @MockBean
    private CustomerService customerService;

    private void setupMocksForVerifierTests() {
        when(customerService.getCustomerById("cust123"))
            .thenReturn(
                new Customer("cust123", new InvoiceAddress("1234AB", "123"))
            );
    }
}
