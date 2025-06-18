// src/App.jsx
import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./Login";
import EventsList from "./EventsList";
import EventDetails from "./EventDetails";

function App() {
  // const [token, setToken] = useState(null);

  // if (!token) {
  //   return <Login onLogin={setToken} />;
  // }

  return (
    <Router>
      <Routes>
        <Route path="/" element={<EventsList />} />
        <Route path="/event/:eventId" element={<EventDetails />} />
      </Routes>
    </Router>
  );
}

export default App;
