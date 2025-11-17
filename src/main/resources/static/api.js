// ======================================================
// GENERIC POST (auto JSON)
// ======================================================
async function apiPost(url, data) {
    const res = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });

    if (!res.ok) {
        const text = await res.text();
        throw new Error("HTTP " + res.status + ": " + text);
    }

    return res.json().catch(() => ({}));
}


// ======================================================
// GENERIC GET (auto JSON)
// ======================================================
async function apiGet(url) {
    const res = await fetch(url, {
        headers: { "Accept": "application/json" }
    });

    if (!res.ok) {
        const text = await res.text();
        throw new Error("HTTP " + res.status + ": " + text);
    }

    return res.json().catch(() => ([]));
}


// ======================================================
// GENERIC FETCH (auto JSON)
// ALWAYS stringifies body if object
// ALWAYS sets Content-Type: application/json for POST/PATCH
// ======================================================
async function apiFetch(url, options = {}) {

    const finalOptions = {
        method: options.method || "GET",
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        body: options.body
    };

    // auto-stringify body
    if (finalOptions.body && typeof finalOptions.body !== "string") {
        finalOptions.body = JSON.stringify(finalOptions.body);
    }

    const res = await fetch(url, finalOptions);

    if (!res.ok) {
        const text = await res.text();
        throw new Error("HTTP " + res.status + ": " + text);
    }

    return res.json().catch(() => ({}));
}
