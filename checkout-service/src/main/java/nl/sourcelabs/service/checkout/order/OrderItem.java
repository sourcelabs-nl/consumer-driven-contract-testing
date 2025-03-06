package nl.sourcelabs.service.checkout.order;

import nl.sourcelabs.service.checkout.product.Product;

public record OrderItem(Product product, Integer quantity) {}
