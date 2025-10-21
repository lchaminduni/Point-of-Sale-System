import { useEffect, useState } from "react";
import axios from "../api/axios";
import Navbar from "../components/Navbar";

export default function SaleItemsPage() {
  const [sales, setSales] = useState([]);
  const [products, setProducts] = useState([]);
  const [saleItems, setSaleItems] = useState([]);
  const [form, setForm] = useState({
    saleId: "",
    productId: "",
    quantity: 1,
    unitPrice: 0,
    totalPrice: 0,
  });
  const [error, setError] = useState("");

  const fetchSales = async () => {
    try {
      const res = await axios.get("/sales");
      setSales(res.data);
    } catch (err) {
      console.error(err);
      setError("Failed to fetch sales");
    }
  };

  const fetchProducts = async () => {
    try {
      const res = await axios.get("/products/active");
      setProducts(res.data);
    } catch (err) {
      console.error(err);
      setError("Failed to fetch products");
    }
  };

  const fetchSaleItems = async (saleId) => {
    if (!saleId) return setSaleItems([]);
    try {
      const res = await axios.get(`/sale-items/sale/${saleId}`);
      setSaleItems(res.data);
    } catch (err) {
      console.error(err);
      setError("Failed to fetch sale items");
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    const newForm = { ...form, [name]: value };

    if (name === "quantity" || name === "unitPrice") {
      const quantity = parseInt(newForm.quantity) || 0;
      const unitPrice = parseFloat(newForm.unitPrice) || 0;
      newForm.totalPrice = quantity * unitPrice;
    }

    setForm(newForm);
  };

  const handleAdd = async () => {
    if (!form.saleId || !form.productId) {
      setError("Please select sale and product");
      return;
    }

    try {
      await axios.post("/sale-items", {
        ...form,
        saleId: parseInt(form.saleId),
        productId: parseInt(form.productId),
        quantity: parseInt(form.quantity),
        unitPrice: parseFloat(form.unitPrice),
        totalPrice: parseFloat(form.totalPrice),
      });

      fetchSaleItems(form.saleId);
      setForm({ saleId: form.saleId, productId: "", quantity: 1, unitPrice: 0, totalPrice: 0 });
      setError("");
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || "Failed to add sale item");
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`/sale-items/${id}`);
      fetchSaleItems(form.saleId);
    } catch (err) {
      console.error(err);
      setError("Failed to delete sale item");
    }
  };

  // ✅ Generate updated receipt PDF
  const handleGenerateReceipt = async () => {
    if (!form.saleId) {
      setError("Select a sale first");
      return;
    }

    try {
      const res = await axios.get(`/sale-items/sale/${form.saleId}/receipt`, {
        responseType: "blob",
      });
      const url = window.URL.createObjectURL(new Blob([res.data], { type: "application/pdf" }));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `sale-${form.saleId}-receipt.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error(err);
      setError("Failed to generate receipt");
    }
  };

  useEffect(() => {
    fetchSales();
    fetchProducts();
  }, []);

  return (
    <div>
      <Navbar />
      <div className="p-8">
        <h1 className="text-3xl font-bold mb-6">Sale Items</h1>
        {error && <div className="mb-4 text-red-600">{error}</div>}

        <div className="mb-6 grid grid-cols-2 gap-4">
          <select
            name="saleId"
            value={form.saleId}
            onChange={(e) => {
              handleChange(e);
              fetchSaleItems(e.target.value);
            }}
            className="border px-4 py-2 rounded"
          >
            <option value="">Select Sale</option>
            {sales.map((s) => (
              <option key={s.id} value={s.id}>
                {s.saleNumber} - {new Date(s.saleDate).toLocaleDateString()}
              </option>
            ))}
          </select>

          <select
            name="productId"
            value={form.productId}
            onChange={handleChange}
            className="border px-4 py-2 rounded"
          >
            <option value="">Select Product</option>
            {products.map((p) => (
              <option key={p.id} value={p.id}>
                {p.name} - Rs. {p.price}
              </option>
            ))}
          </select>

          <input
            name="quantity"
            type="number"
            value={form.quantity}
            onChange={handleChange}
            placeholder="Quantity"
            min={1}
            className="border px-4 py-2 rounded"
          />

          <input
            name="unitPrice"
            type="number"
            value={form.unitPrice}
            onChange={handleChange}
            placeholder="Unit Price"
            className="border px-4 py-2 rounded"
          />

          <input
            name="totalPrice"
            type="number"
            value={form.totalPrice}
            readOnly
            placeholder="Total Price"
            className="border px-4 py-2 rounded bg-gray-100"
          />
        </div>

        <div className="flex gap-4 mb-4">
          <button
            onClick={handleAdd}
            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
          >
            Add Item
          </button>

          <button
            onClick={handleGenerateReceipt}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
          >
            Generate Receipt
          </button>
        </div>

        <table className="w-full border">
          <thead>
            <tr className="bg-gray-100">
              <th className="border px-4 py-2">ID</th>
              <th className="border px-4 py-2">Product</th>
              <th className="border px-4 py-2">Quantity</th>
              <th className="border px-4 py-2">Unit Price</th>
              <th className="border px-4 py-2">Total Price</th>
              <th className="border px-4 py-2">Actions</th>
            </tr>
          </thead>
          <tbody>
            {saleItems.map((item) => (
              <tr key={item.id}>
                <td className="border px-4 py-2">{item.id}</td>
                <td className="border px-4 py-2">{item.productName}</td>
                <td className="border px-4 py-2">{item.quantity}</td>
                <td className="border px-4 py-2">{item.unitPrice}</td>
                <td className="border px-4 py-2">{item.totalPrice}</td>
                <td className="border px-4 py-2">
                  <button
                    onClick={() => handleDelete(item.id)}
                    className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600 transition"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
            {saleItems.length === 0 && (
              <tr>
                <td colSpan={6} className="text-center py-4">
                  No sale items
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
