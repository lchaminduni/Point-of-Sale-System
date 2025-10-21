import { useRef, useState, useEffect } from "react";
import Navbar from "../components/Navbar";
import { motion } from "framer-motion";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

export default function MenuDashboardCarousel() {
  const categories = [
    {
      name: "🥤 Fresh Juices & Smoothies",
      color: "from-yellow-100 to-red-50",
      items: [
        { name: "Mango Juice", price: 350 },
        { name: "Watermelon Juice", price: 350 },
        { name: "Lime Juice", price: 250 },
        { name: "Banana Juice", price: 350 },
        { name: "Papaya Juice", price: 400 },
        { name: "Mix Fruit Juice", price: 400 },
        { name: "Avocado Juice", price: 350 },
        { name: "Passion Fruit Juice", price: 400 },
        { name: "Milkshake (Flavored)", price: 660 },
        { name: "Fruit Shake", price: 660 },
        { name: "Smoothie (Fruit-based)", price: 650 },
        { name: "Faluda", price: 650 },
        { name: "Fruit Salad with Ice Cream", price: 650 },
      ],
    },
    {
      name: "☕ Coffee & Espresso Drinks",
      color: "from-orange-100 to-yellow-50",
      items: [
        { name: "Espresso", price: 350 },
        { name: "Americano", price: 500 },
        { name: "Cappuccino", price: 530 },
        { name: "Latte (Classic)", price: 530 },
        { name: "Flavored Lattes (Caramel, Hazelnut, etc.)", price: 630 },
        { name: "Mocha", price: 560 },
        { name: "Flat White", price: 530 },
        { name: "Iced Coffee / Cold Brew", price: 530 },
        { name: "Affogato", price: 620 },
      ],
    },
    {
      name: "🍵 Tea & Herbal Beverages",
      color: "from-green-100 to-green-50",
      items: [
        { name: "Green Tea", price: 300 },
        { name: "Chai Latte", price: 450 },
        { name: "Matcha Latte", price: 550 },
        { name: "Lemon Ginger Tea", price: 350 },
        { name: "Herbal Teas (Peppermint, etc.)", price: 300 },
      ],
    },
    {
      name: "🍓 Specialty Drinks & Mocktails",
      color: "from-pink-100 to-pink-50",
      items: [
        { name: "Iced Lemonade", price: 450 },
        { name: "Tropical Fizz", price: 550 },
        { name: "Fruit Punch Mocktail", price: 550 },
        { name: "Coffee Smoothie", price: 550 },
      ],
    },
    {
      name: "🥐 Light Snacks & Pastries",
      color: "from-purple-100 to-purple-50",
      items: [
        { name: "Muffins (Blueberry, Chocolate)", price: 250 },
        { name: "Croissants (Plain, Chocolate, Almond)", price: 300 },
        { name: "Bagels (Cream Cheese, Smoked Salmon)", price: 450 },
        { name: "Granola Bars", price: 350 },
        { name: "Fruit Bowls", price: 500 },
        { name: "Yogurt Parfaits", price: 550 },
      ],
    },
  ];

  const scrollRef = useRef(null);

  const scroll = (direction) => {
    if (scrollRef.current) {
      const width = scrollRef.current.offsetWidth;
      scrollRef.current.scrollBy({
        left: direction === "left" ? -width : width,
        behavior: "smooth",
      });
    }
  };

  // ✅ Missing imports fixed here
  const [salesData, setSalesData] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/sales/category-summary")
      .then((res) => res.json())
      .then((data) => setSalesData(data))
      .catch((err) => console.error("Error fetching sales data:", err));
  }, []);

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-purple-100">
      <Navbar />
      <div className="p-10">
        <h1 className="text-4xl font-extrabold text-gray-800 mb-10 text-center">
          Menu & Price List
        </h1>

        {/* Scroll buttons */}
        <div className="relative">
          <button
            onClick={() => scroll("left")}
            className="absolute left-0 top-1/2 -translate-y-1/2 z-10 bg-white p-3 rounded-full shadow-md hover:bg-gray-100 transition"
          >
            ◀
          </button>
          <button
            onClick={() => scroll("right")}
            className="absolute right-0 top-1/2 -translate-y-1/2 z-10 bg-white p-3 rounded-full shadow-md hover:bg-gray-100 transition"
          >
            ▶
          </button>

          <motion.div
            ref={scrollRef}
            className="flex overflow-x-auto gap-6 scrollbar-hide px-12"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.6 }}
          >
            {categories.map((cat, index) => (
              <motion.div
                key={index}
                className={`flex-shrink-0 w-80 h-[600px] bg-gradient-to-br ${cat.color} p-6 rounded-3xl shadow-lg border border-gray-200`}
                whileHover={{ scale: 1.05 }}
                transition={{ type: "spring", stiffness: 200 }}
              >
                <h2 className="text-2xl font-bold mb-4 text-center">{cat.name}</h2>
                <ul className="divide-y divide-gray-300 overflow-y-hidden">
                  {cat.items.map((item, idx) => (
                    <li
                      key={idx}
                      className="py-2 flex justify-between font-medium text-sm hover:text-indigo-700 transition"
                    >
                      <span className="truncate">{item.name}</span>
                      <span>Rs. {item.price}</span>
                    </li>
                  ))}
                </ul>
              </motion.div>
            ))}
          </motion.div>
        </div>

        {/* ✅ Sales Chart Section */}
        <div className="mt-20 bg-white rounded-3xl shadow-lg p-10">
          <h2 className="text-3xl font-bold text-center mb-6 text-gray-800">
            Sales Summary by Category
          </h2>
          <div className="w-full h-[400px]">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={salesData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="category" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="sales" fill="#7c3aed" radius={[10, 10, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
}
