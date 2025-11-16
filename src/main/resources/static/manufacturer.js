console.log("manufacturer.js loaded");

const user = currentUser();

// Load dashboard data
loadProducts();
loadBatches();
loadWarehouses();
loadShipments();

// ---------------- PRODUCTS ------------------

async function loadProducts() {
    console.log("Loading products...");

    let data = [];
    try {
        data = await apiGet("/api/products/all");
    } catch (e) {
        console.error("Error loading products:", e);
    }

    const out = document.getElementById("products-list");
    const sel = document.getElementById("batch-product");

    out.innerHTML = "";
    sel.innerHTML = "";

    if (!data || data.length === 0) {
        out.textContent = "No products available.";
        return;
    }

    let html = "<ul>";
    data.forEach(p => {
        html += `<li><b>${p.name}</b> - ${p.description}</li>`;
        sel.innerHTML += `<option value="${p.productId}">${p.name}</option>`;
    });
    html += "</ul>";

    out.innerHTML = html;
}

async function createProduct() {
    console.log("createProduct() called");

    const name = document.getElementById("prod-name").value;
    const description = document.getElementById("prod-desc").value;

    if (!name) return alert("Please enter a product name");

    const payload = {
        name,
        description,
        manufacturerId: user.userId
    };

    console.log("Sending payload:", payload);

    try {
        const res = await apiPost("/api/products/add", payload);
        console.log("Response:", res);
        alert("Product created!");
    } catch (err) {
        console.error("Error creating product:", err);
        alert("Error creating product.");
    }

    loadProducts();
}


// ---------------- BATCHES ------------------

async function loadBatches() {
    console.log("Loading batches...");

    let data = [];
    try {
        data = await apiGet("/api/batches/all");
    } catch (e) {
        console.error("Error loading batches:", e);
    }

    const out = document.getElementById("batches-list");
    const sel = document.getElementById("ship-batch");

    out.innerHTML = "";
    sel.innerHTML = "";

    if (!data || data.length === 0) {
        out.textContent = "No batches found.";
        return;
    }

    let html = "<ul>";
    data.forEach(b => {
        html += `<li>
            <b>Batch ${b.batchId}</b> —
            Product ${b.productId} |
            Qty=${b.quantity} |
            Mfg=${b.manufactureDate}
        </li>`;

        sel.innerHTML += `<option value="${b.batchId}">Batch ${b.batchId}</option>`;
    });
    html += "</ul>";

    out.innerHTML = html;
}

async function createBatch() {
    const payload = {
        productId: document.getElementById("batch-product").value,
        manufactureDate: document.getElementById("batch-mfg").value,
        expiryDate: document.getElementById("batch-exp").value,
        quantity: document.getElementById("batch-qty").value
    };

    console.log("Creating batch:", payload);

    try {
        await apiPost("/api/batches/add", payload);
        alert("Batch created!");
    } catch (err) {
        console.error("Error creating batch:", err);
        alert("Error creating batch.");
    }

    loadBatches();
}


// ---------------- WAREHOUSES ------------------

async function loadWarehouses() {
    console.log("Loading warehouses...");

    let data = [];
    try {
        data = await apiGet("/api/warehouses/all");
    } catch (e) {
        console.error("Error loading warehouses:", e);
    }

    const sel = document.getElementById("ship-warehouse");
    sel.innerHTML = "";

    if (!data || data.length === 0) {
        sel.innerHTML = "<option>No warehouses</option>";
        return;
    }

    data.forEach(w => {
        sel.innerHTML += `<option value="${w.warehouseId}">${w.location}</option>`;
    });
}


// ---------------- SHIPMENTS ------------------

async function createShipment() {
    const payload = {
        batchId: document.getElementById("ship-batch").value,
        warehouseId: document.getElementById("ship-warehouse").value,
        dispatchDate: document.getElementById("ship-date").value,
        status: "In Transit"
    };

    console.log("Creating shipment:", payload);

    try {
        await apiPost("/api/shipments/add", payload);
        alert("Shipment created!");
    } catch (err) {
        console.error("Error creating shipment:", err);
        alert("Error creating shipment.");
    }

    loadShipments();
}

async function loadShipments() {
    console.log("Loading shipments...");

    let data = [];
    try {
        data = await apiGet("/api/shipments/all");
    } catch (e) {
        console.error("Error loading shipments:", e);
    }

    const out = document.getElementById("shipments-list");
    out.innerHTML = "";

    if (!data || data.length === 0) {
        out.textContent = "No shipments yet.";
        return;
    }

    let html = "<ul>";
    data.forEach(s => {
        html += `<li>
            Shipment ${s.shipmentId} —
            Batch ${s.batchId} →
            Warehouse ${s.warehouseId} |
            Status: ${s.status}
        </li>`;
    });
    html += "</ul>";

    out.innerHTML = html;
}
