package nl.sourcelabs.service.customer;

import nl.sourcelabs.service.customer.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customer_id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customer_id") String customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }
}
