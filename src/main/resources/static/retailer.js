console.log("retailer.js loaded");

const user = currentUser();
console.log("Logged-in retailer:", user);

loadIncoming();
loadAllShipments();

// -------------------------------------------------------
// LOAD SHIPMENTS THAT RETAILER CAN MARK DELIVERED
// -------------------------------------------------------

async function loadIncoming() {
    console.log("Loading incoming retailer shipments...");

    let data = [];
    try {
        data = await apiGet("/api/shipments/all");
    } catch (err) {
        console.error("Error loading shipments:", err);
    }

    const out = document.getElementById("incoming-list");
    out.innerHTML = "";

    // Retailers receive only shipments that are "Received"
    const incoming = data.filter(s => s.status === "Received");

    if (incoming.length === 0) {
        out.textContent = "No shipments awaiting delivery.";
        return;
    }

    let html = "<ul>";
    incoming.forEach(s => {
        html += `
            <li>
                Shipment ${s.shipmentId} — Batch ${s.batchId}  
                <button onclick="deliverShipment(${s.shipmentId})">Mark Delivered</button>
            </li>
        `;
    });
    html += "</ul>";

    out.innerHTML = html;
}


// -------------------------------------------------------
// MARK SHIPMENT DELIVERED
// -------------------------------------------------------

async function deliverShipment(id) {
    console.log("Marking shipment delivered:", id);

    try {
        await apiFetch(`/api/shipments/${id}/deliver`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ userId: user.userId })
        });

        alert("Shipment marked as DELIVERED!");
    } catch (err) {
        console.error("Error delivering shipment:", err);
        alert("Error marking delivery.");
    }

    loadIncoming();
    loadAllShipments();
}


// -------------------------------------------------------
// LOAD ALL SHIPMENTS
// -------------------------------------------------------

async function loadAllShipments() {
    console.log("Loading all shipments...");

    let data = [];
    try {
        data = await apiGet("/api/shipments/all");
    } catch (err) {
        console.error("Error loading shipments:", err);
    }

    const out = document.getElementById("all-shipments-list");
    out.innerHTML = "";

    if (!data || data.length === 0) {
        out.textContent = "No shipments found.";
        return;
    }

    let html = "<ul>";
    data.forEach(s => {
        html += `
            <li>
                Shipment ${s.shipmentId} — Batch ${s.batchId} → Warehouse ${s.warehouseId}
                | Status: <b>${s.status}</b>
            </li>
        `;
    });
    html += "</ul>";

    out.innerHTML = html;
}
