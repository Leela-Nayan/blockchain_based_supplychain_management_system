console.log("auditor.js loaded");

// Ensure only auditor can access
if (!requireAuth([5])) {
    console.warn("Unauthorized access.");
}

// Store all logs
let fullLogs = [];

// Load immediately
loadAuditLogs();


// ======================================================
// LOAD ALL LOGS
// ======================================================
async function loadAuditLogs() {
    const out = document.getElementById("audit-logs");
    out.innerHTML = "Loading...";

    try {
        fullLogs = await apiGet("/api/audit/all");
    } catch (err) {
        console.error("Error loading logs:", err);
        out.textContent = "Error loading logs.";
        return;
    }

    renderLogs(fullLogs);
}


// ======================================================
// RENDER LOG LIST WITH CHECKBOXES
// ======================================================
function renderLogs(logs) {
    const out = document.getElementById("audit-logs");
    out.innerHTML = "";

    if (!logs || logs.length === 0) {
        out.textContent = "No audit logs found.";
        return;
    }

    let html = `
        <div style="margin-bottom:12px;">
            <button onclick="approveSelected()">Approve Selected</button>
            <button onclick="rejectSelected()">Reject Selected</button>
        </div>
        <ul style="list-style:none; padding:0;">
    `;

    logs.forEach(log => {
        html += `
            <li style="margin-bottom:8px; padding:6px; border-bottom:1px solid #ddd;">
                <input type="checkbox" class="log-check" value="${log.logId}">
                <b>[${log.status}]</b>
                — <b>${log.eventType}</b>
                — User: ${log.userId}
                — ${log.details}
                — <i>${log.timestamp}</i>
            </li>
        `;
    });

    html += "</ul>";
    out.innerHTML = html;
}


// ======================================================
// GET SELECTED LOG IDs
// ======================================================
function getSelectedLogIds() {
    return [...document.querySelectorAll(".log-check:checked")]
        .map(chk => parseInt(chk.value));
}


// ======================================================
// APPROVE SELECTED
// ======================================================
async function approveSelected() {
    const ids = getSelectedLogIds();
    if (ids.length === 0) return alert("No logs selected.");

    const auditor = currentUser();

    try {
        await apiFetch("/api/audit/approve/bulk", {
            method: "POST",
            body: JSON.stringify({ logIds: ids, auditorId: auditor.userId })
        });

        alert("Approved!");
        loadAuditLogs();
    } catch (err) {
        console.error(err);
        alert("Error approving logs.");
    }
}


// ======================================================
// REJECT SELECTED
// ======================================================
async function rejectSelected() {
    const ids = getSelectedLogIds();
    if (ids.length === 0) return alert("No logs selected.");

    const auditor = currentUser();

    try {
        await apiFetch("/api/audit/reject/bulk", {
            method: "POST",
            body: JSON.stringify({ logIds: ids, auditorId: auditor.userId })
        });

        alert("Rejected!");
        loadAuditLogs();
    } catch (err) {
        console.error(err);
        alert("Error rejecting logs.");
    }
}


// ======================================================
// SEARCH LOGS
// ======================================================
function filterLogs() {
    const q = document.getElementById("search-box").value.toLowerCase();

    const filtered = fullLogs.filter(log =>
        JSON.stringify(log).toLowerCase().includes(q)
    );

    renderLogs(filtered);
}
