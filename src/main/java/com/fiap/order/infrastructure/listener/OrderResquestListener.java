package com.fiap.order.infrastructure.listener;

import com.fiap.order.core.entity.Order;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
public class OrderResquestListener {

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "${kafka.topic.order-receiver.name}" , partitions = {"${kafka.topic.order-receiver.partition}"})
    })
    public void processOrderRequest(Consumer<String, Order> consumer) {
        // Process the incoming message
        System.out.println("Received message: " + consumer.toString());
    }
}
