// src/Login.jsx
import React, { useState } from "react";

function Login({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    // 👇 Call Keycloak directly, not your Gateway
    const response = await fetch("http://localhost:8091/realms/ticketing-security-realm/protocol/openid-connect/token", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams({
        grant_type: "password",
        client_id: "ticketing-client-id",
        client_secret: "GzaSfUZqbI3SrXEl1Io9R0jwsjRvlOFl",
        username,
        password,
      }),
    });

    if (response.ok) {
      const data = await response.json();
      const token = data.access_token;
      onLogin(token); // ⏪ Save it in parent state
    } else {
      alert("Login failed!");
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ padding: "2rem" }}>
      <h1>Login</h1>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        required
      /><br/>
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      /><br/>
      <button type="submit">Login</button>
    </form>
  );
}

export default Login;
