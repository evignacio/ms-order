package com.fiap.order.infrastructure.listener;

import com.fiap.order.core.dto.CreateOrderDTO;
import com.fiap.order.core.usecase.CreateOrderUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderResquestListener {

    private final CreateOrderUseCase createOrderUseCase;

    public OrderResquestListener(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "${kafka.topic.order-receiver.name}", partitions = {"${kafka.topic.order-receiver.partition}"})
    })
    public void processOrderRequest(CreateOrderDTO input) {
        log.info("Received kafka message: {}", input);
        createOrderUseCase.execute(input);
        log.info("kafka message  processed successfully");
    }
}
