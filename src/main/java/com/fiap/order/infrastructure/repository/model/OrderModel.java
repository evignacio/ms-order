package com.fiap.order.infrastructure.repository.model;

import com.fiap.order.core.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@Document("orders")
public class OrderModel {
    @Id
    private String id;
    private String customerId;
    private Set<OrderItemModel> orderItems;
    private AddressModel address;
    private Status status;
    private Instant createdAt;
}
