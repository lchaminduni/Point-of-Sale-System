import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

export default function ProtectedRoute({ element: Component, allowedRoles }) {
  const token = localStorage.getItem("token");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  try {
    const decoded = jwtDecode(token);
    const userRole = (decoded.role || decoded.roles?.[0] || decoded.authorities?.[0] || "")
      .replace("ROLE_", "");

    //If user role is allowed → show the page
    if (allowedRoles.includes(userRole)) {
      return <Component />;
    } else {
      // If not allowed → redirect to dashboard
      return <Navigate to="/dashboard" replace />;
    }
  } catch (error) {
    console.error("Invalid token", error);
    localStorage.removeItem("token");
    return <Navigate to="/login" replace />;
  }
}
