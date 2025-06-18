import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function EventsList() {
  const [events, setEvents] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://inventory-service:8080/api/v1/inventory/events")
      .then((res) => res.json())
      .then((data) => setEvents(data))
      .catch((err) => console.error("Error fetching events:", err));
  }, []);

  const handleClick = (event) => {
    navigate(`/event/${event.eventId}`);
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h1>Events</h1>
      <ul>
        {events.map((event) => (
          <li
            key={event.eventId}
            style={{ margin: "1rem 0", cursor: "pointer", color: "blue" }}
            onClick={() => handleClick(event)}>
            {event.event} — Remaining Tickets: {event.capacity} — Price: ${event.ticketPrice}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default EventsList;
