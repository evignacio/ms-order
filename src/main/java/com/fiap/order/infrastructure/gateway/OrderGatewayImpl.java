package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.gateway.OrderGateway;
import com.fiap.order.infrastructure.mapper.OrderMapper;
import com.fiap.order.infrastructure.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fiap.order.infrastructure.mapper.OrderMapper.toEntity;
import static com.fiap.order.infrastructure.mapper.OrderMapper.toModel;

@Slf4j
@Component
public class OrderGatewayImpl implements OrderGateway {

    private final OrderRepository orderRepository;

    public OrderGatewayImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(Order order) {
        return toEntity(orderRepository.save(toModel(order)));
    }

    @Override
    public Optional<Order> find(String orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper::toEntity);
    }

    @Override
    public Set<Order> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public void delete(String orderId) {
        orderRepository.deleteById(orderId);
    }
}
