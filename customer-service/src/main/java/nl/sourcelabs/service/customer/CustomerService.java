package nl.sourcelabs.service.customer;

import nl.sourcelabs.service.customer.model.Customer;
import nl.sourcelabs.service.customer.model.InvoiceAddress;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    public Customer getCustomerById(String customerId) {
        return new Customer(customerId, new InvoiceAddress(((int) (Math.random() * 1000)) + "AB", ((int) (Math.random() * 100)) + ""));
    }
}
