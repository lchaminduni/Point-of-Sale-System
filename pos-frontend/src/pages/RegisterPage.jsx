import { useState } from "react";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";

export default function RegisterPage() {
    const navigate = useNavigate();
    const [form, setForm] = useState({
      username: "",
      password: "",
      email: "",
      firstName: "",
      lastName: "",
      role: "CASHIER",
    });
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
  
    const handleChange = (e) => {
      setForm({ ...form, [e.target.name]: e.target.value });
    };
  
    const handleRegister = async (e) => {
      e.preventDefault();
      try {
        await axios.post("/auth/register", form);
        setSuccess("Registration successful! Redirecting to login...");
        setTimeout(() => navigate("/login"), 2000);
      } catch (err) {
        setError(err.response?.data?.message || "Registration failed.");
      }
    };
  
    return (
      <div
        className="flex items-center justify-center h-screen bg-cover bg-center"
        style={{ backgroundImage: "url('/Background_img.png')" }}
      >
        <div className="bg-white/90 p-10 rounded-2xl shadow-2xl max-w-sm w-full text-center">
          <h1 className="text-3xl font-bold mb-6 text-gray-800">Register</h1>
          <form onSubmit={handleRegister}>
            <input
              type="text"
              name="username"
              placeholder="Username"
              className="w-full mb-4 px-4 py-2 border rounded-md"
              value={form.username}
              onChange={handleChange}
              required
            />
            <input
              type="password"
              name="password"
              placeholder="Password"
              className="w-full mb-4 px-4 py-2 border rounded-md"
              value={form.password}
              onChange={handleChange}
              required
            />
            <input
              type="email"
              name="email"
              placeholder="Email"
              className="w-full mb-4 px-4 py-2 border rounded-md"
              value={form.email}
              onChange={handleChange}
              required
            />
            <input
              type="text"
              name="firstName"
              placeholder="First Name"
              className="w-full mb-4 px-4 py-2 border rounded-md"
              value={form.firstName}
              onChange={handleChange}
              required
            />
            <input
              type="text"
              name="lastName"
              placeholder="Last Name"
              className="w-full mb-4 px-4 py-2 border rounded-md"
              value={form.lastName}
              onChange={handleChange}
              required
            />
            <select
              name="role"
              className="w-full mb-4 px-4 py-2 border rounded-md"
              value={form.role}
              onChange={handleChange}
            >
              <option value="CASHIER">CASHIER</option>
              <option value="ADMIN">ADMIN</option>
            </select>
  
            <button
              type="submit"
              className="w-full bg-green-600 hover:bg-green-700 text-white py-2 rounded-md transition duration-200"
            >
              Register
            </button>
          </form>
          {error && <p className="text-red-500 mt-4">{error}</p>}
          {success && <p className="text-green-500 mt-4">{success}</p>}
        </div>
      </div>
    );
}
