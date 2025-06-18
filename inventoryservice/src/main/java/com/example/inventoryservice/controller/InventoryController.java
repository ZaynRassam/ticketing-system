package com.example.inventoryservice.controller;

import com.example.inventoryservice.response.EventInventoryResponse;
import com.example.inventoryservice.response.VenueInventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InventoryController {

    private InventoryService inventoryService;
    @Autowired
    public InventoryController(final InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory/events")
    public @ResponseBody List<EventInventoryResponse> inventoryGetAllEvents() {
        return inventoryService.getAllEvents();
    }

    @GetMapping("/inventory/venue/{venueId}")
    public @ResponseBody VenueInventoryResponse venueInventoryByVenueId(@PathVariable("venueId") Long venueId){
        return inventoryService.getVenueInformation(venueId);
    }

    @GetMapping("/inventory/venues")
    public @ResponseBody List<VenueInventoryResponse> inventoryGetAllVenues() {
        return inventoryService.getAllVenues();
    }

    @GetMapping("/inventory/event/{eventId}")
    public @ResponseBody EventInventoryResponse eventInventoryByEventId(@PathVariable("eventId") Long eventId){
        return inventoryService.getEventInformation(eventId);
    }

    @PutMapping("/inventory/event/{eventId}/capacity/{capacity}")
    public ResponseEntity<Void> updateEventCapacity(@PathVariable("eventId") Long eventId, @PathVariable("capacity") Long ticketCount){
        inventoryService.updateEventCapacity(eventId, ticketCount);
        return ResponseEntity.ok().build();
    }

}
