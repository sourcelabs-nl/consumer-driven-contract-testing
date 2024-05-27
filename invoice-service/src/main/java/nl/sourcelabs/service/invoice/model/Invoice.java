package nl.sourcelabs.service.invoice.model;

public record Invoice(Long amountInCents, String date, String description, String customerId, InvoiceAddress invoiceAddress) {
}