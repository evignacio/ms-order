package com.fiap.order.infrastructure.repository;

import com.fiap.order.infrastructure.repository.model.OrderModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderModel, String> {
}
