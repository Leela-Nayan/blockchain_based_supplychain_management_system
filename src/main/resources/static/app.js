/* app.js — complete frontend integration + form validation
   For Blockchain-Based Supply Chain Management System
*/

const API = {
    products: '/api/products',
    batches: '/api/batches',
    warehouses: '/api/warehouses',
    shipments: '/api/shipments',
    transactions: '/api/transactions',
    blockchain: '/api/blockchain',
    reports: '/api/reports'
};

document.addEventListener('DOMContentLoaded', () => {
    setupNav();
    setupForms();
    showView('dashboard');
    preloadSelects();
});

/* NAVIGATION */
function setupNav() {
    document.querySelectorAll('nav button').forEach(btn => {
        btn.addEventListener('click', () => showView(btn.dataset.view));
    });
}

/* VIEW SWITCHER */
function showView(name) {
    document.querySelectorAll('.view').forEach(v => v.classList.add('hidden'));
    const el = document.getElementById('view-' + name);
    if (el) el.classList.remove('hidden');
    if (name === 'products') fetchProducts();
    if (name === 'batches') fetchBatches();
    if (name === 'warehouses') fetchWarehouses();
    if (name === 'shipments') fetchShipments();
    if (name === 'transactions') fetchTransactions();
}

/* FORM HANDLERS */
function setupForms() {

    // Create Product
    document.getElementById('form-create-product').addEventListener('submit', async e => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const name = fd.get('name').trim();
        const manufacturerId = parseInt(fd.get('manufacturerId'));

        if (!name) return alert('Product name is required');
        if (!manufacturerId || manufacturerId <= 0) return alert('Manufacturer ID must be positive');

        const body = {
            name,
            description: fd.get('description'),
            manufacturerId
        };
        await postJSON(API.products + '/add', body);
        alert('✅ Product created');
        e.target.reset();
        preloadSelects();
    });

    // Create User
    document.getElementById('form-create-user').addEventListener('submit', async e => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const email = fd.get('email').trim();
        if (!email.includes('@')) return alert('Please enter a valid email');
        const body = {
            name: fd.get('name'),
            email,
            role_id: parseInt(fd.get('roleId'))
        };
        await postJSON('/api/users/add', body);
        alert('✅ User created');
        e.target.reset();
    });

    // Create Batch
    document.getElementById('form-create-batch').addEventListener('submit', async e => {
        e.preventDefault();
        const form = e.target;
        const qty = parseInt(form.quantity.value);
        if (qty <= 0) return alert('Quantity must be greater than zero');
        if (!form.manufacture_date.value) return alert('Manufacture date is required');

        const body = {
            productId: parseInt(document.getElementById('batch-product-select').value),
            manufactureDate: form.manufacture_date.value,
            expiryDate: form.expiry_date.value || null,
            quantity: qty
        };
        await postJSON(API.batches + '/add', body);
        alert('✅ Batch created');
        form.reset();
        fetchBatches();
    });

    // Create Warehouse
    document.getElementById('form-create-warehouse').addEventListener('submit', async e => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const location = fd.get('location').trim();
        const capacity = parseInt(fd.get('capacity'));

        if (!location) return alert('Location is required');
        if (capacity <= 0) return alert('Capacity must be positive');

        await postJSON(API.warehouses + '/add', { location, capacity });
        alert('✅ Warehouse added');
        e.target.reset();
        fetchWarehouses();
        preloadSelects();
    });

    // Create Shipment
    document.getElementById('form-create-shipment').addEventListener('submit', async e => {
        e.preventDefault();
        const f = e.target;
        const bId = parseInt(document.getElementById('shipment-batch-select').value);
        const wId = parseInt(document.getElementById('shipment-warehouse-select').value);
        if (!f.dispatch_date.value) return alert('Dispatch date is required');

        const body = {
            batchId: bId,
            warehouseId: wId,
            dispatchDate: f.dispatch_date.value,
            deliveryDate: f.delivery_date.value || null,
            status: f.status.value
        };
        await postJSON(API.shipments + '/add', body);
        alert('✅ Shipment created');
        f.reset();
        fetchShipments();
    });

    // Create Transaction
    document.getElementById('form-create-transaction').addEventListener('submit', async e => {
        e.preventDefault();
        const f = e.target;
        const sender = parseInt(f.sender_id.value);
        const receiver = parseInt(f.receiver_id.value);
        if (sender <= 0 || receiver <= 0) return alert('Sender and receiver IDs must be valid');

        const body = {
            productId: parseInt(document.getElementById('txn-product-select').value),
            senderId: sender,
            receiverId: receiver,
            details: f.details.value
        };
        await postJSON(API.transactions + '/add', body);
        alert('✅ Transaction created');
        f.reset();
        fetchTransactions();
    });

    // Refresh buttons
    document.getElementById('btn-refresh-products').addEventListener('click', fetchProducts);
    document.getElementById('btn-refresh-batches').addEventListener('click', fetchBatches);
    document.getElementById('btn-refresh-warehouses').addEventListener('click', fetchWarehouses);
    document.getElementById('btn-refresh-shipments').addEventListener('click', fetchShipments);
    document.getElementById('btn-refresh-transactions').addEventListener('click', fetchTransactions);

    // Blockchain Trace
    document.getElementById('btn-get-trace').addEventListener('click', async () => {
        const q = document.getElementById('trace-product-id').value.trim();
        if (!q) return alert('Enter product or transaction ID');
        const res = await getJSON(`${API.blockchain}/trace?productId=${encodeURIComponent(q)}`);
        document.getElementById('blockchain-output').textContent = JSON.stringify(res, null, 2);
    });

    // Reports
    document.getElementById('btn-report-shipments-monthly').addEventListener('click', async () => {
        const r = await getJSON(API.reports + '/shipments/monthly');
        renderReport(r);
    });
    document.getElementById('btn-report-top-products').addEventListener('click', async () => {
        const r = await getJSON(API.reports + '/products/top');
        renderReport(r);
    });
    document.getElementById('btn-report-warehouse-util').addEventListener('click', async () => {
        const r = await getJSON(API.reports + '/warehouses/utilization');
        renderReport(r);
    });
}

/* DROPDOWNS */
async function preloadSelects() {
    await fetchProducts().then(() => {
        const products = window._products || [];
        const pSel = document.getElementById('batch-product-select');
        const txnSel = document.getElementById('txn-product-select');
        if (pSel) {
            pSel.innerHTML = '';
            products.forEach(p => {
                const o = document.createElement('option');
                o.value = p.productId || p.product_id;
                o.textContent = p.name;
                pSel.appendChild(o);
            });
        }
        if (txnSel) {
            txnSel.innerHTML = '';
            products.forEach(p => {
                const o = document.createElement('option');
                o.value = p.productId || p.product_id;
                o.textContent = p.name;
                txnSel.appendChild(o);
            });
        }
    });

    const wSel = document.getElementById('shipment-warehouse-select');
    if (wSel) {
        const ws = await getJSON(API.warehouses + '/all');
        wSel.innerHTML = '';
        (ws || []).forEach(w => {
            const o = document.createElement('option');
            o.value = w.warehouseId || w.warehouse_id;
            o.textContent = w.location;
            wSel.appendChild(o);
        });
    }

    const bSel = document.getElementById('shipment-batch-select');
    if (bSel) {
        const batches = await getJSON(API.batches + '/all');
        bSel.innerHTML = '';
        (batches || []).forEach(b => {
            const o = document.createElement('option');
            o.value = b.batchId || b.batch_id;
            o.textContent = `#${o.value} - product ${b.productId || b.product_id}`;
            bSel.appendChild(o);
        });
    }
}

/* FETCH FUNCTIONS */
async function fetchProducts() {
    const data = await getJSON(API.products + '/all');
    window._products = data || [];
    const tbody = document.querySelector('#tbl-products tbody');
    tbody.innerHTML = '';
    (data || []).forEach(p => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${p.productId || p.product_id}</td>
      <td>${escapeText(p.name)}</td>
      <td>${escapeText(p.description || '')}</td>
      <td>${p.manufacturerId || p.manufacturer_id || ''}</td>`;
        tbody.appendChild(tr);
    });
    return data;
}

async function fetchBatches() {
    const data = await getJSON(API.batches + '/all');
    const tbody = document.querySelector('#tbl-batches tbody');
    tbody.innerHTML = '';
    (data || []).forEach(b => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${b.batchId || b.batch_id}</td>
      <td>${b.productId || b.product_id}</td>
      <td>${b.manufactureDate || b.manufacture_date}</td>
      <td>${b.expiryDate || b.expiry_date || ''}</td>
      <td>${b.quantity || 0}</td>`;
        tbody.appendChild(tr);
    });
    return data;
}

async function fetchWarehouses() {
    const data = await getJSON(API.warehouses + '/all');
    const tbody = document.querySelector('#tbl-warehouses tbody');
    tbody.innerHTML = '';
    (data || []).forEach(w => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${w.warehouseId || w.warehouse_id}</td>
      <td>${escapeText(w.location)}</td>
      <td>${w.capacity || 0}</td>`;
        tbody.appendChild(tr);
    });
    return data;
}

async function fetchShipments() {
    const data = await getJSON(API.shipments + '/all');
    const tbody = document.querySelector('#tbl-shipments tbody');
    tbody.innerHTML = '';
    (data || []).forEach(s => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${s.shipmentId || s.shipment_id}</td>
      <td>${s.batchId || s.batch_id}</td>
      <td>${s.warehouseId || s.warehouse_id}</td>
      <td>${s.dispatchDate || s.dispatch_date}</td>
      <td>${s.deliveryDate || s.delivery_date || ''}</td>
      <td>${s.status || ''}</td>`;
        tbody.appendChild(tr);
    });
    return data;
}

async function fetchTransactions() {
    const data = await getJSON(API.transactions + '/all');
    const tbody = document.querySelector('#tbl-transactions tbody');
    tbody.innerHTML = '';
    (data || []).forEach(t => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${t.txnId || t.txn_id}</td>
      <td>${t.productId || t.product_id}</td>
      <td>${t.senderId || t.sender_id}</td>
      <td>${t.receiverId || t.receiver_id}</td>
      <td>${escapeText(t.details || '')}</td>
      <td>${t.createdAt || t.created_at || ''}</td>`;
        tbody.appendChild(tr);
    });
    return data;
}

/* REPORTS */
function renderReport(rows) {
    const out = document.getElementById('report-output');
    out.innerHTML = '';
    if (!rows || rows.length === 0) {
        out.textContent = 'No data';
        return;
    }
    const table = document.createElement('table');
    table.className = 'data-table';
    const thead = document.createElement('thead');
    const tbody = document.createElement('tbody');
    const keys = Object.keys(rows[0]);
    thead.innerHTML = '<tr>' + keys.map(k => `<th>${k}</th>`).join('') + '</tr>';
    rows.forEach(r => {
        const tr = document.createElement('tr');
        tr.innerHTML = keys.map(k => `<td>${escapeText(String(r[k] ?? ''))}</td>`).join('');
        tbody.appendChild(tr);
    });
    table.appendChild(thead);
    table.appendChild(tbody);
    out.appendChild(table);
}

/* FETCH WRAPPERS */
async function getJSON(url) {
    try {
        const r = await fetch(url, { credentials: 'same-origin' });
        if (!r.ok) throw new Error(`HTTP ${r.status}`);
        return await r.json();
    } catch (e) {
        alert('⚠️ Failed: ' + e.message);
        return [];
    }
}

async function postJSON(url, body) {
    try {
        const r = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
            credentials: 'same-origin'
        });
        if (!r.ok) {
            const text = await r.text();
            alert(`⚠️ ${r.status} ${r.statusText}\n${text}`);
            return null;
        }
        return await r.json().catch(() => null);
    } catch (e) {
        alert('⚠️ Network error: ' + e.message);
        return null;
    }
}

/* UTILITY */
function escapeText(s) {
    return String(s || '').replace(/[&<>]/g, ch => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;' }[ch]));
}
