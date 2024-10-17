package nl.sourcelabs.service.invoice;

import nl.sourcelabs.service.invoice.model.Invoice;
import nl.sourcelabs.service.invoice.model.NewInvoiceRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final CustomerServiceClient customerService;
    private final InvoiceService invoiceService;

    public InvoiceController(CustomerServiceClient customerService, InvoiceService invoiceService) {
        this.customerService = customerService;
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<Invoice> newInvoice(@RequestBody NewInvoiceRequest request) {
        var customer = customerService.getCustomerById(request.customerId());

        var invoice = new Invoice(request.invoiceAmountInCents(), request.invoiceDate(), request.description(), customer.customerId(), customer.invoiceAddress());
        var invoiceId = invoiceService.createInvoice(invoice);

        return ResponseEntity.created(URI.create("/invoices/" + invoiceId)).body(invoice);
    }

    @GetMapping("/{invoice_id}")
    public Invoice findInvoiceById(@PathVariable("invoice_id") String invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }
}
