package nl.sourcelabs.service.invoice;

import nl.sourcelabs.service.invoice.model.Invoice;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InvoiceService {

    private final Map<String, Invoice> invoices = new ConcurrentHashMap<String, Invoice>();

    public String createInvoice(Invoice invoice) {
        // Do something with this
        var invoiceId = UUID.randomUUID().toString();
        invoices.put(invoiceId, invoice);
        return invoiceId;
    }

    public Invoice getInvoiceById(String invoiceId) {
        return invoices.get(invoiceId);
    }
}
