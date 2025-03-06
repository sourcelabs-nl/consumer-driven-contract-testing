package nl.sourcelabs.service.checkout.product;

import jakarta.validation.constraints.NotNull;

public record Product(@NotNull String id, @NotNull String title, @NotNull Long priceCents) {}