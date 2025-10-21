import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode"; 
import { LogOut, UserCircle } from "lucide-react";

export default function Navbar() {
  const navigate = useNavigate();
  const [user, setUser] = useState({ name: "", role: "" });

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        setUser({
          name: decoded.firstName
            ? decoded.firstName + " " + decoded.lastName
            : decoded.sub || decoded.username || "User",
          role: (decoded.role || decoded.roles?.[0] || decoded.authorities?.[0] || "Cashier")
                  .replace("ROLE_", ""),
        });
      } catch (err) {
        console.error("Invalid token", err);
      }
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  const isCashier = user.role.toLowerCase() === "cashier";

  return (
    <nav className="bg-gradient-to-r from-blue-600 to-indigo-700 text-white px-6 py-4 flex justify-between items-center shadow-lg">
      <h1 className="font-bold text-2xl tracking-wide">Blend & Brew</h1>

      <div className="flex items-center space-x-6">
        {/* Common link for all users */}
        <Link to="/dashboard" className="hover:underline">Home</Link>

        {/* Only show these if NOT a cashier */}
        {!isCashier && (
          <>
            <Link to="/categories" className="hover:underline">Categories</Link>
            <Link to="/products" className="hover:underline">Products</Link>
            <Link to="/sale-items" className="hover:underline">Sale Items</Link>
            <Link to="/reports" className="hover:underline">Reports</Link>
          </>
        )}

        {/* Cashier and Admin can both access Sales */}
        <Link to="/sales" className="hover:underline">Sales</Link>

        {/* User info */}
        <div className="flex items-center gap-3 bg-white/20 px-3 py-1 rounded-md">
          <UserCircle size={20} />
          <span className="text-sm">{user.name} ({user.role})</span>
        </div>

        {/* Logout */}
        <button
          onClick={handleLogout}
          className="bg-red-500 hover:bg-red-600 px-3 py-1 rounded-md transition"
        >
          <LogOut size={18} className="inline mr-1" /> Sign Out
        </button>
      </div>
    </nav>
  );
}
