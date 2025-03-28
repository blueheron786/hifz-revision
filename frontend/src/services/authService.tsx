import axios from "axios";

// Talks to the back-end to authenticate, gets a JWT token
const API_URL = "http://localhost:8080/api/auth";

export const login = async (username: string, password: string) => {
  const response = await axios.post(`${API_URL}/login`, { username, password });
  const token = response.data.token;
  
  if (token) {
    localStorage.setItem("token", token); // Store token for future requests
  }
  
  return token;
};

export const logout = () => {
  localStorage.removeItem("token"); // Clear token on logout
};

export const getToken = () => localStorage.getItem("token"); // Retrieve token when needed
