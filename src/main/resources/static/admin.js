console.log("admin.js loaded");

// Require admin role
if (!requireAuth([1])) {}

const who = currentUser();
console.log("Logged-in admin:", who);

// --------------------------------------------------
// BUTTON HANDLERS
// --------------------------------------------------
document.getElementById("btn-add-user").addEventListener("click", () => {
    window.location.href = "/register.html";
});

document.getElementById("btn-refresh").addEventListener("click", loadUsers);

// --------------------------------------------------
// INITIAL LOAD
// --------------------------------------------------
loadUsers();
startRejectedAuditPolling();   // auto-check rejected logs every 12 seconds



// ==================================================
// LOAD USERS
// ==================================================
async function loadUsers() {
    console.log("Loading users...");

    let data = [];
    try {
        data = await apiGet("/api/users/all");
    } catch (err) {
        console.error("Error fetching users:", err);
    }

    const out = document.getElementById("users-output");
    out.innerHTML = "";

    if (!data || data.length === 0) {
        out.textContent = "No users found.";
        return;
    }

    let html = `
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role ID</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
    `;

    data.forEach(u => {
        html += `
            <tr>
                <td>${u.userId}</td>
                <td>${u.name}</td>
                <td>${u.email}</td>
                <td>${u.roleId}</td>
                <td>
                    <button onclick="deleteUser(${u.userId})">Delete</button>
                </td>
            </tr>
        `;
    });

    html += "</tbody></table>";

    out.innerHTML = html;
}



// ==================================================
// DELETE USER
// ==================================================
async function deleteUser(id) {
    if (!confirm("Are you sure you want to delete this user?")) return;

    try {
        await apiFetch(`/api/users/${id}`, { method: "DELETE" });
        alert("User deleted");
    } catch (err) {
        alert("Error deleting user");
        console.error(err);
    }

    loadUsers();
}



// ===================================================================
// 🔥 REJECTED AUDIT LOGS POLLING SYSTEM
// ===================================================================

let lastRejectedCount = 0;

function startRejectedAuditPolling() {
    fetchRejectedAudits();
    setInterval(fetchRejectedAudits, 12000);  // every 12 seconds
}


// ==================================================
// FETCH REJECTED AUDIT LOGS
// ==================================================
async function fetchRejectedAudits() {
    let rejected = [];

    try {
        rejected = await apiGet("/api/audit/rejected");
    } catch (err) {
        console.error("Error fetching rejected audit logs:", err);
        return;
    }

    const count = rejected.length;

    // Update badge count
    const badge = document.getElementById("rejected-badge");
    if (badge) badge.textContent = count;

    // Alert only when new logs are added
    if (count > lastRejectedCount) {
        if (lastRejectedCount !== 0) {
            showRejectionAlert(rejected);
        }
    }

    lastRejectedCount = count;

    updateRejectedPanel(rejected);
}



// ==================================================
// SHOW ALERT WHEN NEW REJECTIONS HAPPEN
// ==================================================
function showRejectionAlert(rejected) {
    alert(`⚠️ ${rejected.length} audit log(s) were REJECTED by an auditor.`);
}



// ==================================================
// UPDATE REJECTED LOGS PANEL
// ==================================================
function updateRejectedPanel(list) {
    const panel = document.getElementById("rejected-panel");
    if (!panel) return;

    if (list.length === 0) {
        panel.innerHTML = "<p>No rejected audits.</p>";
        return;
    }

    let html = "<ul>";

    list.forEach(r => {
        html += `
            <li style="margin-bottom:10px;">
                <b>Log #${r.logId}</b> — <span style="color:#b00;">${r.details}</span><br/>
                <b>Rejected By:</b> ${r.auditedBy}<br/>
                <b>Time:</b> ${formatIST(r.auditDecisionTs)}
            </li>
        `;
    });

    html += "</ul>";
    panel.innerHTML = html;
}



// ==================================================
// TIMESTAMP FORMATTER (IST)
// ==================================================
function formatIST(ts) {
    if (!ts) return "";
    const d = new Date(ts);
    return d.toLocaleString("en-IN", { timeZone: "Asia/Kolkata", hour12: true });
}
