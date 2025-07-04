import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";

function EventDetails() {
  const { eventId } = useParams();
  const [event, setEvent] = useState(null);
  const [venue, setVenue] = useState(null);
  const [numTickets, setNumTickets] = useState(1);
  const [status, setStatus] = useState("");

  useEffect(() => {
    fetch(`/inventory-api/v1/inventory/event/${eventId}`)
      .then((res) => res.json())
      .then((data) => {
        setEvent(data);
        setVenue(data.venue);
      })
      .catch((err) => console.error("Error fetching event details:", err));
  }, [eventId]);
  
  const handleBuyTickets = async () => {
    if (numTickets < 1) {
      setStatus("Please enter a valid ticket quantity.");
      return;
    }
  
    try {
      setStatus("Checking availability...");
  
      // ✅ 1) Always get latest event before submitting
      const freshRes = await fetch(`/inventory-api/v1/inventory/event/${eventId}`);
      const freshEvent = await freshRes.json();
      console.log(freshRes)
      console.log(freshEvent)

      if (numTickets > freshEvent.capacity) {
        setStatus(`Only ${freshEvent.capacity} tickets available.`);
        return;
      }
  
      setStatus("Processing order...");
  
      const token = localStorage.getItem("token");
  
      const response = await fetch(`/booking-api/v1/booking`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          userId: 1,
          eventId: eventId,
          ticketCount: numTickets,
        }),
      });
      console.log(response)
      if (response.ok) {
        setStatus("Booking successful! Refreshing...");
  
        // ✅ HARD REFRESH to guarantee consistency
        window.location.reload();
  
      } else {
        setStatus("Booking failed. Please try again.");
      }
  
    } catch (err) {
      console.error(err);
      setStatus("Error submitting order.");
    }
  };
  

  if (!event || !venue) return <p>Loading event and venue details...</p>;

  return (
    <div style={{ padding: "2rem" }}>
      <h1>Venue Details</h1>
      <p><strong>Event:</strong> {event.event}</p>
      <p><strong>Venue Name:</strong> {venue.name}</p>
      <p><strong>Total Capacity:</strong> {venue.totalCapacity}</p>
      <p><strong>Remaining Capacity:</strong> {event.capacity}</p>
      <p><strong>Ticket Price:</strong> ${event.ticketPrice}</p>

      <div style={{ marginTop: "1rem" }}>
        <h3>Buy Tickets</h3>
        <input
          type="number"
          min="1"
          value={numTickets}
          onChange={(e) => setNumTickets(parseInt(e.target.value, 10))}
        />{" "}
        <button onClick={handleBuyTickets}>Buy</button>
        <p>{status}</p>
      </div>

      <Link to="/">← Back to Events</Link>
    </div>
  );
}

export default EventDetails;
