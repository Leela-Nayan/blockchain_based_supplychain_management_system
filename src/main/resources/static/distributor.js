console.log("distributor.js loaded");

const user = currentUser();
console.log("Logged-in distributor:", user);

loadIncomingShipments();
loadAllShipments();

// -------------------------------------------------------
// LOAD INCOMING (shipments not received yet)
// -------------------------------------------------------

async function loadIncomingShipments() {
    console.log("Loading incoming shipments...");

    let data = [];
    try {
        data = await apiGet("/api/shipments/all");
    } catch (err) {
        console.error("Error:", err);
    }

    const out = document.getElementById("incoming-list");
    out.innerHTML = "";

    // Filter shipments that are NOT received yet
    const incoming = data.filter(s => s.status === "In Transit");

    if (incoming.length === 0) {
        out.textContent = "No incoming shipments.";
        return;
    }

    let html = "<ul>";
    incoming.forEach(s => {
        html += `
            <li>
                Shipment ${s.shipmentId} — Batch ${s.batchId}
                <button onclick="receiveShipment(${s.shipmentId})">Mark Received</button>
            </li>
        `;
    });
    html += "</ul>";

    out.innerHTML = html;
}


// -------------------------------------------------------
// RECEIVE SHIPMENT
// -------------------------------------------------------

async function receiveShipment(id) {
    console.log("Receiving shipment:", id);

    try {
        await apiFetch(`/api/shipments/${id}/receive`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ userId: user.userId })
        });

        alert("Shipment marked as RECEIVED!");
    } catch (err) {
        console.error("Error receiving shipment:", err);
        alert("Error receiving shipment.");
    }

    loadIncomingShipments();
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
        console.error("Error:", err);
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
