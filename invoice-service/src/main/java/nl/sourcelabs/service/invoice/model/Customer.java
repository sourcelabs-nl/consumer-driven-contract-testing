package nl.sourcelabs.service.invoice.model;

import jakarta.validation.constraints.NotNull;

public record Customer(@NotNull String customerId, @NotNull InvoiceAddress invoiceAddress) {
}