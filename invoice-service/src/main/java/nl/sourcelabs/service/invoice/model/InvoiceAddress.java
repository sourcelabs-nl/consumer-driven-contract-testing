package nl.sourcelabs.service.invoice.model;

import jakarta.validation.constraints.NotNull;

public record InvoiceAddress(
    @NotNull String postalCode,
    @NotNull String houseNumber) {
}