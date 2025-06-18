package com.example.orderservice.service;

import com.example.bookingservice.event.BookingEvent;
import com.example.orderservice.client.InventoryServiceClient;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopicName;
    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaGroupId;

    private OrderRepository orderRepository;
    private InventoryServiceClient InventoryServiceClient;
    @Autowired
    public OrderService(OrderRepository orderRepository, InventoryServiceClient inventoryServiceClient){
        this.orderRepository = orderRepository;
        this.InventoryServiceClient = inventoryServiceClient;
    }

    @KafkaListener(topics = kafkaTopicName, groupId = kafkaGroupId)
    public void orderEvent(BookingEvent bookingEvent) {
        log.info("Received booking event: {}", bookingEvent);

        Order order  = createOrder(bookingEvent);
        orderRepository.saveAndFlush(order);

        InventoryServiceClient.updateInventory(order.getEventId(), order.getQuantity());
        log.info("{} tickets booked for eventId eventId: {}", order.getQuantity(), order.getEventId());
    }

    public Order createOrder(BookingEvent bookingEvent){
        return Order.builder()
                .eventId(bookingEvent.getEventId())
                .customerId(bookingEvent.getUserId())
                .quantity(bookingEvent.getTicketCount())
                .total(bookingEvent.getTotalPrice())
                .build();
    }
}
