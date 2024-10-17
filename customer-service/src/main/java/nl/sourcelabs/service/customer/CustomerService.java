package nl.sourcelabs.service.customer;

import nl.sourcelabs.service.customer.model.Customer;
import nl.sourcelabs.service.customer.model.InvoiceAddress;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CustomerService {

    public Customer getCustomerById(String customerId) {
        return new Customer(customerId, randomAddress());
    }

    private InvoiceAddress randomAddress() {
        var random = new Random();
        return new InvoiceAddress(random.nextInt(1000) * 1000 + "AB", random.nextInt(100) + "");
    }
}
