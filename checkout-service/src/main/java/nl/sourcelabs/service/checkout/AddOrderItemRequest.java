package nl.sourcelabs.service.checkout;

public record AddOrderItemRequest(String productId, int quantity) {}
