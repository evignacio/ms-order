package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.OrderItemDTO;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.gateway.ProductGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Service
public class CreateItemsOrderUseCase {

    private final ProductGateway productGateway;

    public CreateItemsOrderUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public Set<OrderItem> execute(Set<OrderItemDTO> input) {
        var orderItems = new LinkedHashSet<OrderItem>();
        for (OrderItemDTO item : input) {
            var product = productGateway.find(item.sku())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found, sku:" + item.sku()));

            var orderItem = new OrderItem(item.sku(), item.amount(), product.value());
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
