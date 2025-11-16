/* app.js — Full Frontend Logic */

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
    document.querySelectorAll('nav button[data-view]').forEach(btn => {
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

/* LOGIN MODAL */
function openLogin() {
    document.getElementById('login-modal').classList.remove('hidden');
}

function closeLogin() {
    document.getElementById('login-modal').classList.add('hidden');
}

/* FORM HANDLERS */
function setupForms() {

    // LOGIN
    document.getElementById('form-login').addEventListener('submit', async e => {
        e.preventDefault();
        const fd = new FormData(e.target);

        const body = {
            email: fd.get('email'),
            password: fd.get('password')
        };

        const res = await postJSON('/api/login', body);

        if (!res || res.error) {
            alert('❌ Invalid email or password');
            return;
        }

        alert(`✅ Logged in as ${res.name}`);
        closeLogin();
    });

    // CREATE USER
    document.getElementById('form-create-user').addEventListener('submit', async e => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const email = fd.get('email').trim();

        const body = {
            name: fd.get('name'),
            email,
            password: fd.get('password'),
            roleId: parseInt(fd.get('roleId'))
        };

        const res = await postJSON('/api/users/add', body);

        if (res && !res.error) {
            alert('✅ User created');
            e.target.reset();
        }
    });

    // CREATE PRODUCT
    document.getElementById('form-create-product').addEventListener('submit', async e => {
        e.preventDefault();
        const fd = new FormData(e.target);

        const body = {
            name: fd.get('name'),
            description: fd.get('description'),
            manufacturerId: parseInt(fd.get('manufacturerId'))
        };

        await postJSON(API.products + '/add', body);
        alert('✅ Product created');
        e.target.reset();
        preloadSelects();
    });

    // CREATE BATCH
    document.getElementById('form-create-batch').addEventListener('submit', async e => {
        e.preventDefault();
        const f = e.target;

        const body = {
            productId: parseInt(document.getElementById('batch-product-select').value),
            manufactureDate: f.manufacture_date.value,
            expiryDate: f.expiry_date.value || null,
            quantity: parseInt(f.quantity.value)
        };

        await postJSON(API.batches + '/add', body);
        alert('✅ Batch created');
        f.reset();
        fetchBatches();
    });

    // CREATE WAREHOUSE
    document.getElementById('form-create-warehouse').addEventListener('submit', async e => {
        e.preventDefault();

        const fd = new FormData(e.target);
        const body = {
            location: fd.get('location'),
            capacity: parseInt(fd.get('capacity'))
        };

        await postJSON(API.warehouses + '/add', body);
        alert('✅ Warehouse added');
        e.target.reset();
        fetchWarehouses();
        preloadSelects();
    });

    // CREATE SHIPMENT
    document.getElementById('form-create-shipment').addEventListener('submit', async e => {
        e.preventDefault();
        const f = e.target;

        const body = {
            batchId: parseInt(document.getElementById('shipment-batch-select').value),
            warehouseId: parseInt(document.getElementById('shipment-warehouse-select').value),
            dispatchDate: f.dispatch_date.value,
            deliveryDate: f.delivery_date.value || null,
            status: f.status.value
        };

        await postJSON(API.shipments + '/add', body);
        alert('✅ Shipment created');
        f.reset();
        fetchShipments();
    });

    // CREATE TRANSACTION
    document.getElementById('form-create-transaction').addEventListener('submit', async e => {
        e.preventDefault();
        const f = e.target;

        const body = {
            productId: parseInt(document.getElementById('txn-product-select').value),
            senderId: parseInt(f.sender_id.value),
            receiverId: parseInt(f.receiver_id.value),
            details: f.details.value
        };

        await postJSON(API.transactions + '/add', body);
        alert('✅ Transaction created');
        f.reset();
        fetchTransactions();
    });

    // REFRESH BUTTONS
    document.getElementById('btn-refresh-products').addEventListener('click', fetchProducts);
    document.getElementById('btn-refresh-batches').addEventListener('click', fetchBatches);
    document.getElementById('btn-refresh-warehouses').addEventListener('click', fetchWarehouses);
    document.getElementById('btn-refresh-shipments').addEventListener('click', fetchShipments);
    document.getElementById('btn-refresh-transactions').addEventListener('click', fetchTransactions);

    // BLOCKCHAIN TRACE
    document.getElementById('btn-get-trace').addEventListener('click', async () => {
        const q = document.getElementById('trace-product-id').value.trim();
        if (!q) return alert('Enter product or transaction ID');
        const res = await getJSON(`${API.blockchain}/trace?productId=${encodeURIComponent(q)}`);
        document.getElementById('blockchain-output').textContent = JSON.stringify(res, null, 2);
    });
}

/* DROPDOWNS */
async function preloadSelects() {
    const products = await fetchProducts();
    const pSel = document.getElementById('batch-product-select');
    const txnSel = document.getElementById('txn-product-select');

    if (pSel) {
        pSel.innerHTML = '';
        (products || []).forEach(p => {
            const o = document.createElement('option');
            o.value = p.productId || p.product_id;
            o.textContent = p.name;
            pSel.appendChild(o);
        });
    }
    if (txnSel) {
        txnSel.innerHTML = '';
        (products || []).forEach(p => {
            const o = document.createElement('option');
            o.value = p.productId || p.product_id;
            o.textContent = p.name;
            txnSel.appendChild(o);
        });
    }

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

/* FETCH HELPERS */
async function getJSON(url) {
    try {
        const r = await fetch(url);
        if (!r.ok) throw new Error(`HTTP ${r.status}`);
        return await r.json();
    } catch (err) {
        alert('⚠️ Failed: ' + err.message);
        return [];
    }
}

async function postJSON(url, body) {
    try {
        const r = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        const text = await r.text();
        if (!r.ok) {
            alert(`⚠️ ${r.status} Error\n${text}`);
            return null;
        }

        try {
            return JSON.parse(text);
        } catch {
            return null;
        }

    } catch (err) {
        alert('⚠️ Network error: ' + err.message);
        return null;
    }
}

/* UTIL */
function escapeText(s) {
    return String(s || '').replace(/[&<>]/g, ch => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;' }[ch]));
}
