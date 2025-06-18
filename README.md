## Event Booker
### Spring Boot Microservices Project

This repo contains several microservices to allow a user to view and purchase tickets to a fictitious event happening at a local venue. 

All microservices have been containerised and can be deployed using this command whilst in the root directory of the project:

```
docker compose -v up -d --build
```

The frontend has been built in React (+Vite) and can be accessed with this link:

```http://localhost:5173```

The services use Kafka as a way of booking the events. When a user "purchases" tickets, the ```Frontend``` services makes 
a ```GET``` request to the ```Inventory``` service to check if there are valid number of tickets available to purchase, 
it then sends a ```POST``` request to the ```Booking``` service with the ```userID```, ```eventId``` and ```ticketNumber```.
The booking services does another check to ensure enough tickets are available by calling the ```Inventory``` service, if yes,
the booking is sent to a Kafka topic.

The ```Order``` service is in charge of collecting the Kafka messages from the topic and processing the bookings. 
It then calls the ```Inventory``` service to update the total number of tickets available to be purchased. 

### TODO:

1. Currently, bookings can be made whilst orders are sitting in the Kafka topic to be consumed and executed, this means,
a booking can sell a ticket already reserved for another user. Tickets that have been booked should update the inventory
to prevent this.


