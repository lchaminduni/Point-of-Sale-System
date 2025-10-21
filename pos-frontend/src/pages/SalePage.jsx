import { useEffect, useState } from "react";
import axios from "../api/axios";
import Navbar from "../components/Navbar"; // Add Navbar

export default function SalePage() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const [itemForm, setItemForm] = useState({
    productId: "",
    quantity: 1,
    unitPrice: 0,
    totalPrice: 0,
  });
  const [saleForm, setSaleForm] = useState({
    customerName: "",
    customerPhone: "",
    paymentMethod: "CASH",
    discountAmount: 0,
    notes: "",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const fetchProducts = async () => {
    try {
      const res = await axios.get("/products/active");
      setProducts(res.data);
    } catch (err) {
      console.error(err);
      setError("Failed to fetch products");
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleItemChange = (e) => {
    const { name, value } = e.target;
    const updated = { ...itemForm, [name]: value };
    if (name === "quantity" || name === "unitPrice") {
      const quantity = parseFloat(updated.quantity) || 0;
      const unitPrice = parseFloat(updated.unitPrice) || 0;
      updated.totalPrice = (quantity * unitPrice).toFixed(2);
    }
    setItemForm(updated);
  };

  const addItemToCart = () => {
    if (!itemForm.productId) {
      setError("Select a product first");
      return;
    }
    setCart([...cart, { ...itemForm, id: Date.now() }]);
    setItemForm({ productId: "", quantity: 1, unitPrice: 0, totalPrice: 0 });
    setError("");
  };

  const removeItemFromCart = (id) => {
    setCart(cart.filter((i) => i.id !== id));
  };

  const handleSaleChange = (e) => {
    const { name, value } = e.target;
    setSaleForm({ ...saleForm, [name]: value });
  };

  const handleSubmitSale = async () => {
    if (cart.length === 0) {
      setError("Add at least one item to the sale");
      return;
    }

    const payload = {
      ...saleForm,
      discountAmount: parseFloat(saleForm.discountAmount) || 0,
      items: cart.map((item) => ({
        productId: parseInt(item.productId),
        quantity: parseInt(item.quantity),
        unitPrice: parseFloat(item.unitPrice),
      })),
    };

    try {
      const res = await axios.post("/sales", payload);
      const sale = res.data;

      setCart([]);
      setSaleForm({
        customerName: "",
        customerPhone: "",
        paymentMethod: "CASH",
        discountAmount: 0,
        notes: "",
      });
      setSuccess("Sale created successfully!");
      setError("");

      generateReceipt(sale.id);

    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || "Failed to create sale");
      setSuccess("");
    }
  };

  const generateReceipt = async (saleId) => {
    try {
      const res = await axios.get(`/sales/${saleId}/receipt`, { responseType: "blob" });
      const url = window.URL.createObjectURL(new Blob([res.data], { type: "application/pdf" }));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `Sale_${saleId}_Receipt.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error(err);
      setError("Failed to generate receipt PDF");
    }
  };

  return (
    <div>
      <Navbar />
      <div className="p-8">
        <h1 className="text-2xl font-bold mb-4">Create Sale</h1>
        {error && <p className="text-red-600">{error}</p>}
        {success && <p className="text-green-600">{success}</p>}

        {/* Sale Info */}
        <div className="grid grid-cols-2 gap-4 mb-6">
          <input
            type="text"
            name="customerName"
            value={saleForm.customerName}
            onChange={handleSaleChange}
            placeholder="Customer Name"
            className="border px-3 py-2 rounded"
          />
          <input
            type="text"
            name="customerPhone"
            value={saleForm.customerPhone}
            onChange={handleSaleChange}
            placeholder="Customer Phone"
            className="border px-3 py-2 rounded"
          />
          <select
            name="paymentMethod"
            value={saleForm.paymentMethod}
            onChange={handleSaleChange}
            className="border px-3 py-2 rounded"
          >
            <option value="CASH">Cash</option>
            <option value="CARD">Card</option>
            <option value="ONLINE">Online</option>
          </select>
          <input
            type="number"
            name="discountAmount"
            value={saleForm.discountAmount}
            onChange={handleSaleChange}
            placeholder="Discount"
            className="border px-3 py-2 rounded"
          />
        </div>

        {/* Items Section */}
        <h2 className="font-bold text-xl mb-2">Add Items</h2>
        <div className="grid grid-cols-3 gap-3 mb-4">
          <select
            name="productId"
            value={itemForm.productId}
            onChange={handleItemChange}
            className="border px-3 py-2 rounded"
          >
            <option value="">Select Product</option>
            {products.map((p) => (
              <option key={p.id} value={p.id}>
                {p.name} - Rs.{p.price}
              </option>
            ))}
          </select>
          <input
            type="number"
            name="quantity"
            value={itemForm.quantity}
            onChange={handleItemChange}
            placeholder="Qty"
            min={1}
            className="border px-3 py-2 rounded"
          />
          <input
            type="number"
            name="unitPrice"
            value={itemForm.unitPrice}
            onChange={handleItemChange}
            placeholder="Unit Price"
            className="border px-3 py-2 rounded"
          />
        </div>
        <button
          onClick={addItemToCart}
          className="bg-blue-600 text-white px-4 py-2 rounded mb-4 hover:bg-blue-700"
        >
          Add to Cart
        </button>

        {/* Cart */}
        <table className="w-full border mb-4">
          <thead>
            <tr className="bg-gray-200">
              <th className="border px-2 py-1">Product</th>
              <th className="border px-2 py-1">Qty</th>
              <th className="border px-2 py-1">Unit</th>
              <th className="border px-2 py-1">Total</th>
              <th className="border px-2 py-1">Action</th>
            </tr>
          </thead>
          <tbody>
            {cart.length === 0 ? (
              <tr>
                <td colSpan="5" className="text-center py-3">
                  No items added
                </td>
              </tr>
            ) : (
              cart.map((item) => {
                const product = products.find(
                  (p) => p.id === parseInt(item.productId)
                );
                return (
                  <tr key={item.id}>
                    <td className="border px-2 py-1">{product?.name || ""}</td>
                    <td className="border px-2 py-1">{item.quantity}</td>
                    <td className="border px-2 py-1">{item.unitPrice}</td>
                    <td className="border px-2 py-1">{item.totalPrice}</td>
                    <td className="border px-2 py-1 text-center">
                      <button
                        onClick={() => removeItemFromCart(item.id)}
                        className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                      >
                        Remove
                      </button>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>

        <button
          onClick={handleSubmitSale}
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
        >
          Submit Sale & Generate Receipt
        </button>
      </div>
    </div>
  );
}
