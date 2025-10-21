import { useEffect, useState } from "react";
import axios from "../api/axios";
import Navbar from "../components/Navbar";

export default function ProductsPage() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState({
    name: "",
    description: "",
    barcode: "", // optional
    categoryId: "",
    price: 0,
    costPrice: 0,
    stockQuantity: 0,
    minStockLevel: 10,
    isActive: true,
  });
  const [inventoryForm, setInventoryForm] = useState({
    productId: "",
    quantity: 0,
    refType: "ADJUSTMENT",
    notes: "",
  });
  const [successMessage, setSuccessMessage] = useState("");

  const fetchProducts = async () => {
    const res = await axios.get("/products");
    setProducts(res.data);
  };

  const fetchCategories = async () => {
    const res = await axios.get("/categories");
    setCategories(res.data);
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleInventoryChange = (e) => {
    setInventoryForm({ ...inventoryForm, [e.target.name]: e.target.value });
  };

  const handleAddProduct = async () => {
    try {
      const res = await axios.post("/products", form);
      const newProduct = res.data;

      // Reset form but keep the generated barcode in message
      setForm({
        name: "",
        description: "",
        barcode: "",
        categoryId: "",
        price: 0,
        costPrice: 0,
        stockQuantity: 0,
        minStockLevel: 10,
        isActive: true,
      });

      setSuccessMessage(`Product added successfully! Barcode: ${newProduct.barcode}`);
      fetchProducts();

      // Clear message after 3 seconds
      setTimeout(() => setSuccessMessage(""), 3000);
    } catch (err) {
      console.error(err);
    }
  };

  const handleDeleteProduct = async (id) => {
    await axios.delete(`/products/${id}`);
    fetchProducts();
  };

  const handleAddStock = async () => {
    if (!inventoryForm.productId || inventoryForm.quantity <= 0) return;
    await axios.post("/inventory/add-stock", null, { params: inventoryForm });
    setInventoryForm({ productId: "", quantity: 0, refType: "ADJUSTMENT", notes: "" });
    fetchProducts();
  };

  const handleRemoveStock = async () => {
    if (!inventoryForm.productId || inventoryForm.quantity <= 0) return;
    await axios.post("/inventory/remove-stock", null, { params: inventoryForm });
    setInventoryForm({ productId: "", quantity: 0, refType: "ADJUSTMENT", notes: "" });
    fetchProducts();
  };

  useEffect(() => {
    fetchProducts();
    fetchCategories();
  }, []);

  return (
    <div>
      <Navbar />
      <div className="p-8">
        <h1 className="text-3xl font-bold mb-6">Products & Inventory</h1>

        {/* Product Form */}
        <div className="mb-2 grid grid-cols-2 gap-4">
          <input name="name" value={form.name} onChange={handleChange} placeholder="Name" className="border px-4 py-2 rounded" />
          <input name="description" value={form.description} onChange={handleChange} placeholder="Description" className="border px-4 py-2 rounded" />
          <input name="barcode" value={form.barcode} onChange={handleChange} placeholder="Barcode (optional)" className="border px-4 py-2 rounded" />
          <select name="categoryId" value={form.categoryId} onChange={handleChange} className="border px-4 py-2 rounded">
            <option value="">Select Category</option>
            {categories.map((c) => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </select>
          <input name="price" type="number" value={form.price} onChange={handleChange} placeholder="Price" className="border px-4 py-2 rounded" />
          <input name="costPrice" type="number" value={form.costPrice} onChange={handleChange} placeholder="Cost Price" className="border px-4 py-2 rounded" />
          <input name="stockQuantity" type="number" value={form.stockQuantity} onChange={handleChange} placeholder="Stock Quantity" className="border px-4 py-2 rounded" />
          <input name="minStockLevel" type="number" value={form.minStockLevel} onChange={handleChange} placeholder="Min Stock Level" className="border px-4 py-2 rounded" />
        </div>
        <button onClick={handleAddProduct} className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition mb-2">Add Product</button>

        {/* Success Message */}
        {successMessage && <p className="text-green-500 mb-4">{successMessage}</p>}

        {/* Inventory Adjustment */}
        <div className="mb-6 grid grid-cols-4 gap-4 border p-4 rounded bg-gray-50">
          <select name="productId" value={inventoryForm.productId} onChange={handleInventoryChange} className="border px-4 py-2 rounded">
            <option value="">Select Product</option>
            {products.map((p) => (
              <option key={p.id} value={p.id}>{p.name}</option>
            ))}
          </select>
          <input name="quantity" type="number" value={inventoryForm.quantity} onChange={handleInventoryChange} placeholder="Quantity" className="border px-4 py-2 rounded" />
          <input name="notes" value={inventoryForm.notes} onChange={handleInventoryChange} placeholder="Notes" className="border px-4 py-2 rounded" />
          <div className="flex gap-2">
            <button onClick={handleAddStock} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">Add Stock</button>
            <button onClick={handleRemoveStock} className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition">Remove Stock</button>
          </div>
        </div>

        {/* Product Table */}
        <table className="w-full border">
          <thead>
            <tr className="bg-gray-100">
              <th className="border px-4 py-2">ID</th>
              <th className="border px-4 py-2">Name</th>
              <th className="border px-4 py-2">Category</th>
              <th className="border px-4 py-2">Price</th>
              <th className="border px-4 py-2">Stock</th>
              <th className="border px-4 py-2">Actions</th>
            </tr>
          </thead>
          <tbody>
            {products.map((p) => (
              <tr key={p.id}>
                <td className="border px-4 py-2">{p.id}</td>
                <td className="border px-4 py-2">{p.name}</td>
                <td className="border px-4 py-2">{categories.find(c => c.id === p.categoryId)?.name || "-"}</td>
                <td className="border px-4 py-2">{p.price}</td>
                <td className="border px-4 py-2">{p.stockQuantity}</td>
                <td className="border px-4 py-2">
                  <button onClick={() => handleDeleteProduct(p.id)} className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600 transition">Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
