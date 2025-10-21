import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import Dashboard from "./pages/Dashboard";
import RegisterPage from "./pages/RegisterPage";
import CategoriesPage from "./pages/CategoriesPage";
import ProductsPage from "./pages/ProductPage";
import SaleItemsPage from "./pages/SaleItemPage";
import SalePage from "./pages/SalePage";
import SaleReportPage from "./pages/SaleReportPage";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <Router>
      <Routes>
        {/* Default redirect */}
        <Route path="/" element={<Navigate to="/login" replace />} />

        {/* Public routes */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* Protected routes */}
        <Route
          path="/dashboard"
          element={<ProtectedRoute element={Dashboard} allowedRoles={["ADMIN", "CASHIER"]} />}
        />
        <Route
          path="/sales"
          element={<ProtectedRoute element={SalePage} allowedRoles={["ADMIN", "CASHIER"]} />}
        />

        {/* Admin-only routes */}
        <Route
          path="/categories"
          element={<ProtectedRoute element={CategoriesPage} allowedRoles={["ADMIN"]} />}
        />
        <Route
          path="/products"
          element={<ProtectedRoute element={ProductsPage} allowedRoles={["ADMIN"]} />}
        />
        <Route
          path="/sale-items"
          element={<ProtectedRoute element={SaleItemsPage} allowedRoles={["ADMIN"]} />}
        />
        <Route
          path="/reports"
          element={<ProtectedRoute element={SaleReportPage} allowedRoles={["ADMIN"]} />}
        />
      </Routes>
    </Router>
  );
}

export default App;
