async function doLogin() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const res = await apiPost("/api/login", { email, password });

    if (res.error) {
        alert("Invalid email or password");
        return;
    }

    saveUser(res);

    // Role-based redirect
    switch (res.roleId) {
        case 1: window.location.href = "/admin.html"; break;
        case 2: window.location.href = "/manufacturer.html"; break;
        case 3: window.location.href = "/distributor.html"; break;
        case 4: window.location.href = "/retailer.html"; break;
        case 5: window.location.href = "/auditor.html"; break;
        default: window.location.href = "/index.html";
    }
}
