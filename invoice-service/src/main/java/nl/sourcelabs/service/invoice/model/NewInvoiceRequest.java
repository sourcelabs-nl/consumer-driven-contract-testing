package nl.sourcelabs.service.invoice.model;

public record NewInvoiceRequest(String customerId, Long invoiceAmountInCents, String invoiceDate, String description) {
}