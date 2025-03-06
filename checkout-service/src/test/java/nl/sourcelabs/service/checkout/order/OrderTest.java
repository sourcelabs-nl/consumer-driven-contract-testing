package nl.sourcelabs.service.checkout.order;

import nl.sourcelabs.service.checkout.product.Product;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    private Order order;

    @Test
    public void orderTotalCalculationTest() {
        // given
        var items = List.of(
          new OrderItem(new Product("1", "some title", 100L), 2)
        );
        // when
        order = new Order("1", "1", items);
        // then
        assertThat(order.getOrderPriceCents()).isEqualTo(200L);
    }
}
