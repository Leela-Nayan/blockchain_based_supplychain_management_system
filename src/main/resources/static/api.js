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

async function apiGet(url) {
    const res = await fetch(url);

    if (!res.ok) {
        const text = await res.text();
        throw new Error("HTTP " + res.status + ": " + text);
    }

    return res.json().catch(() => ([]));
}

async function apiFetch(url, options = {}) {
    const res = await fetch(url, options);

    if (!res.ok) {
        const text = await res.text();
        throw new Error("HTTP " + res.status + ": " + text);
    }

    return res.json().catch(() => ({}));
}
