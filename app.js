const API = "/api";
let toastTimer;

function showToast(message) {
  const toast = document.getElementById("toast");
  toast.textContent = message;
  toast.classList.add("show");
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => toast.classList.remove("show"), 3200);
}

async function request(url, options = {}) {
  const response = await fetch(url, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options
  });
  if (!response.ok) {
    let message = "Request failed. Please try again.";
    try {
      const body = await response.json();
      message = body.detail || body.message || body.error || message;
      if (body.errors) message = Object.values(body.errors).join(", ");
    } catch (_) {
      const text = await response.text();
      if (text) message = text;
    }
    throw new Error(message);
  }
  if (response.status === 204) return null;
  return response.json();
}

async function loadEquipment() {
  const container = document.getElementById("equipmentList");
  try {
    const equipment = await request(`${API}/equipment`);
    if (!equipment.length) {
      container.innerHTML = "<p>No equipment available. Add equipment using the admin section.</p>";
      return;
    }
    container.innerHTML = equipment.map(item => `
      <article class="equipment-card">
        <div class="machine-icon">${iconFor(item.category)}</div>
        <span class="tag">${escapeHtml(item.category)}</span>
        <h3>${escapeHtml(item.name)}</h3>
        <div class="equipment-meta"><span>📍 ${escapeHtml(item.location)}</span><span>${item.available ? "Available" : "Unavailable"}</span></div>
        <div class="equipment-meta"><span class="price">₹${Number(item.pricePerHour).toLocaleString("en-IN")}</span><span>/ hour</span></div>
        ${item.available ? `<button class="primary-btn" onclick="openBooking(${item.id}, '${escapeJs(item.name)}', ${item.pricePerHour})">Book Now</button>` : '<button class="primary-btn" disabled>Unavailable</button>'}
        <button class="small-btn" style="margin-top:9px" onclick="deleteEquipment(${item.id})">Delete (Admin)</button>
      </article>`).join("");
  } catch (error) {
    container.innerHTML = `<p>Could not load equipment: ${escapeHtml(error.message)}</p>`;
  }
}

function iconFor(category) {
  const value = (category || "").toLowerCase();
  if (value.includes("harvest")) return "🌾";
  if (value.includes("excavat")) return "🚜";
  if (value.includes("implement")) return "⚙️";
  if (value.includes("generator")) return "🔌";
  return "🚜";
}

function escapeHtml(value) {
  return String(value ?? "").replace(/[&<>"']/g, char => ({
    "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
  })[char]);
}
function escapeJs(value) {
  return String(value ?? "").replace(/\\/g, "\\\\").replace(/'/g, "\\'").replace(/[\r\n]/g, " ");
}

function openBooking(id, name, price) {
  const form = document.getElementById("bookingForm");
  form.elements.equipmentId.value = id;
  form.elements.hours.value = 1;
  document.getElementById("selectedEquipment").textContent = `${name} – ₹${price} per hour`;
  document.getElementById("bookingModal").classList.add("show");
}
function closeBooking() {
  document.getElementById("bookingModal").classList.remove("show");
}

document.getElementById("bookingForm").addEventListener("submit", async event => {
  event.preventDefault();
  const form = event.currentTarget;
  const data = {
    equipmentId: Number(form.elements.equipmentId.value),
    customerName: form.elements.customerName.value.trim(),
    phone: form.elements.phone.value.trim(),
    hours: Number(form.elements.hours.value)
  };
  try {
    await request(`${API}/bookings`, { method: "POST", body: JSON.stringify(data) });
    showToast("Booking confirmed successfully!");
    form.reset();
    closeBooking();
    await loadBookings();
  } catch (error) {
    showToast(error.message);
  }
});

document.getElementById("equipmentForm").addEventListener("submit", async event => {
  event.preventDefault();
  const form = event.currentTarget;
  const data = {
    name: form.elements.name.value.trim(),
    category: form.elements.category.value,
    pricePerHour: Number(form.elements.pricePerHour.value),
    location: form.elements.location.value.trim(),
    available: true
  };
  try {
    await request(`${API}/equipment`, { method: "POST", body: JSON.stringify(data) });
    showToast("Equipment added successfully!");
    form.reset();
    await loadEquipment();
  } catch (error) {
    showToast(error.message);
  }
});

async function deleteEquipment(id) {
  if (!confirm("Are you sure you want to delete this equipment?")) return;
  try {
    await request(`${API}/equipment/${id}`, { method: "DELETE" });
    showToast("Equipment deleted.");
    await loadEquipment();
  } catch (error) {
    showToast(error.message);
  }
}

async function loadBookings() {
  const tbody = document.getElementById("bookingRows");
  try {
    const bookings = await request(`${API}/bookings`);
    if (!bookings.length) {
      tbody.innerHTML = '<tr><td colspan="8">No bookings yet.</td></tr>';
      return;
    }
    tbody.innerHTML = bookings.map(booking => {
      const date = booking.bookingTime ? new Date(booking.bookingTime).toLocaleString("en-IN") : "-";
      const cancelled = booking.status === "CANCELLED";
      return `<tr>
        <td>${booking.id}</td><td>${escapeHtml(booking.customerName)}</td>
        <td>${escapeHtml(booking.equipment?.name || "-")}</td><td>${booking.hours}</td>
        <td>₹${Number(booking.totalPrice).toLocaleString("en-IN")}</td><td>${date}</td>
        <td><span class="status ${cancelled ? "cancelled" : ""}">${escapeHtml(booking.status)}</span></td>
        <td>${cancelled ? "-" : `<button class="small-btn" onclick="cancelBooking(${booking.id})">Cancel</button>`}</td>
      </tr>`;
    }).join("");
  } catch (error) {
    tbody.innerHTML = `<tr><td colspan="8">${escapeHtml(error.message)}</td></tr>`;
  }
}

async function cancelBooking(id) {
  if (!confirm("Cancel this booking?")) return;
  try {
    await request(`${API}/bookings/${id}/cancel`, { method: "PUT" });
    showToast("Booking cancelled.");
    await loadBookings();
  } catch (error) {
    showToast(error.message);
  }
}

loadEquipment();
loadBookings();
