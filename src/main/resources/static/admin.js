console.log("admin.js loaded");

if (!requireAuth([1])) {}

const who = currentUser();
console.log("Logged-in admin:", who);

document.getElementById("btn-add-user").addEventListener("click", () => {
    window.location.href = "/register.html";
});

document.getElementById("btn-refresh").addEventListener("click", loadUsers);

loadUsers();

// ----------------------------------------------------
// LOAD USERS
// ----------------------------------------------------

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


// ----------------------------------------------------
// DELETE USER
// ----------------------------------------------------

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
