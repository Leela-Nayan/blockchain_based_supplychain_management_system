console.log("auditor.js loaded");

let fullLogs = [];

loadAuditLogs();

// ------------------------------------------------------
// LOAD ALL AUDIT LOGS
// ------------------------------------------------------

async function loadAuditLogs() {
    const out = document.getElementById("audit-logs");
    out.innerHTML = "Loading...";

    try {
        fullLogs = await apiGet("/api/audit/all");
    } catch (err) {
        console.error("Error loading audit logs:", err);
        out.textContent = "Error loading logs.";
        return;
    }

    renderLogs(fullLogs);
}


// ------------------------------------------------------
// CONVERT TIMESTAMP TO IST
// ------------------------------------------------------

function toIST(timestamp) {
    if (!timestamp) return "";
    const date = new Date(timestamp);

    return date.toLocaleString("en-IN", {
        timeZone: "Asia/Kolkata",
        hour12: true
    });
}


// ------------------------------------------------------
// RENDER AUDIT LOGS
// ------------------------------------------------------

function renderLogs(logs) {
    const out = document.getElementById("audit-logs");
    out.innerHTML = "";

    if (!logs || logs.length === 0) {
        out.textContent = "No audit logs found.";
        return;
    }

    let html = "<ul>";

    logs.forEach(log => {
        const eventType = log.eventtype || log.eventType || "Unknown";
        const userId = log.userid || log.userId || "Unknown";
        const timestamp = log.timestamp || log.audit_timestamp || "";
        const details = log.details || "";

        html += `
            <li>
                <b>${eventType}</b>
                — User: ${userId}
                — ${details}
                — <i>${toIST(timestamp)}</i>
            </li>
        `;
    });

    html += "</ul>";
    out.innerHTML = html;
}


// ------------------------------------------------------
// SEARCH / FILTER LOGS
// ------------------------------------------------------

function filterLogs() {
    const q = document.getElementById("search-box").value.toLowerCase();

    const filtered = fullLogs.filter(log =>
        JSON.stringify(log).toLowerCase().includes(q)
    );

    renderLogs(filtered);
}
