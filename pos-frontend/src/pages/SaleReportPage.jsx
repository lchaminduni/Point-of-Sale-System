import { useState } from "react";
import axios from "../api/axios";
import Navbar from "../components/Navbar"; // Add Navbar

export default function SaleReportPage() {
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [revenueReport, setRevenueReport] = useState(null);
  const [cashierPerformance, setCashierPerformance] = useState([]);
  const [error, setError] = useState("");

  const fetchReport = async () => {
    if (!startDate || !endDate) {
      setError("Please select both start and end dates");
      return;
    }

    try {
      setError("");

      const revenueRes = await axios.get(
        `/sales/report/revenue?start=${startDate}&end=${endDate}`
      );
      setRevenueReport(revenueRes.data);

      const cashierRes = await axios.get(
        `/sales/report/cashier-performance?start=${startDate}&end=${endDate}`
      );
      setCashierPerformance(cashierRes.data);

    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || "Failed to fetch report");
    }
  };

  return (
    <div>
      <Navbar />
      <div className="p-8">
        <h1 className="text-3xl font-bold mb-6">Sales Report</h1>
        {error && <div className="mb-4 text-red-600">{error}</div>}

        <div className="mb-6 grid grid-cols-2 gap-4">
          <input
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
            className="border px-4 py-2 rounded"
          />
          <input
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            className="border px-4 py-2 rounded"
          />
        </div>

        <button
          onClick={fetchReport}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition mb-6"
        >
          Generate Report
        </button>

        {revenueReport && (
          <div className="mb-8">
            <h2 className="text-xl font-bold mb-2">Revenue Report</h2>
            <table className="w-full border">
              <tbody>
                <tr>
                  <td className="border px-4 py-2 font-semibold">Total Revenue</td>
                  <td className="border px-4 py-2">Rs. {revenueReport.totalRevenue.toFixed(2)}</td>
                </tr>
                <tr>
                  <td className="border px-4 py-2 font-semibold">Average Order Value</td>
                  <td className="border px-4 py-2">Rs. {revenueReport.avgOrderValue.toFixed(2)}</td>
                </tr>
                <tr>
                  <td className="border px-4 py-2 font-semibold">Total Sales</td>
                  <td className="border px-4 py-2">{revenueReport.totalSales}</td>
                </tr>
              </tbody>
            </table>
          </div>
        )}

        {cashierPerformance.length > 0 && (
          <div>
            <h2 className="text-xl font-bold mb-2">Cashier Performance</h2>
            <table className="w-full border">
              <thead>
                <tr className="bg-gray-100">
                  <th className="border px-4 py-2">Cashier</th>
                  <th className="border px-4 py-2">Number of Sales</th>
                  <th className="border px-4 py-2">Total Revenue</th>
                </tr>
              </thead>
              <tbody>
                {cashierPerformance.map((c, index) => (
                  <tr key={index}>
                    <td className="border px-4 py-2">{c[0]}</td>
                    <td className="border px-4 py-2">{c[1]}</td>
                    <td className="border px-4 py-2">Rs. {c[2]?.toFixed(2) || 0}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
