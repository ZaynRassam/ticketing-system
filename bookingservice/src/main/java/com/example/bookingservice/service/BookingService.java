package com.example.bookingservice.service;

import com.example.bookingservice.client.InventoryServiceClient;
import com.example.bookingservice.entity.Customer;
import com.example.bookingservice.event.BookingEvent;
import com.example.bookingservice.repository.CustomerRepository;
import com.example.bookingservice.request.BookingRequest;
import com.example.bookingservice.response.BookingResponse;
import com.example.bookingservice.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingService {

    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopicName;

    private CustomerRepository customerRepository;
    private InventoryServiceClient inventoryServiceClient;
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;
    @Autowired
    public BookingService(final CustomerRepository customerRepository, final InventoryServiceClient inventoryServiceClient, KafkaTemplate kafkaTemplate){
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(final BookingRequest bookingRequest) {

//        check if customer exists
        final Customer customer = customerRepository.findById(bookingRequest.getUserId()).orElse(null);
        if (customer == null){
            throw new RuntimeException("User not found");
        }

        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(bookingRequest.getEventId());
        log.info("Inventory Response: " + inventoryResponse);

//        check if enough tickets are available for booking request
        if (inventoryResponse.getCapacity() < bookingRequest.getTicketCount()) {
            throw new RuntimeException("Not enough tickets");
        }

//        create booking event
        final BookingEvent bookingEvent = createBookingEvent(customer, bookingRequest, inventoryResponse);


//        send booking to Order Service on a Kafka topic
        kafkaTemplate.send(kafkaTopicName, bookingEvent);
        log.info("Booking sent to Kafka: {}", bookingEvent);
        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .totalPrice(bookingEvent.getTotalPrice())
                .ticketCount(bookingRequest.getTicketCount())
                .build();
    }

    public BookingEvent createBookingEvent(Customer customer, BookingRequest bookingRequest, InventoryResponse inventoryResponse) {
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(bookingRequest.getEventId())
                .ticketCount(bookingRequest.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice().multiply(BigDecimal.valueOf(bookingRequest.getTicketCount())))
                .build();
    }
}
