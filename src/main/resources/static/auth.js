// Handles session storage & role-based access

function currentUser() {
    const raw = localStorage.getItem("user");
    if (!raw) return null;
    try { return JSON.parse(raw); }
    catch { return null; }
}

function saveUser(user) {
    localStorage.setItem("user", JSON.stringify(user));
}

function doLogout() {
    localStorage.removeItem("user");
    window.location.href = "/login.html";
}

// Protect pages based on role
function requireAuth(allowedRoles) {
    const user = currentUser();

    if (!user) {
        window.location.href = "/login.html";
        return false;
    }

    if (allowedRoles && !allowedRoles.includes(user.roleId)) {
        alert("Unauthorized");
        window.location.href = "/login.html";
        return false;
    }

    return true;
}
