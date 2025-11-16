console.log("register.js loaded");

async function doRegister() {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const roleId = document.getElementById("role").value;

    if (!name || !email || !password) {
        alert("Fill in all fields");
        return;
    }

    const payload = {
        name,
        email,
        password,
        roleId: parseInt(roleId)
    };

    console.log("Registering:", payload);

    try {
        const res = await apiPost("/api/users/add", payload);

        if (res.error) {
            alert("Error: " + res.error);
            return;
        }

        alert("User created!");
        window.location.href = "/admin.html";

    } catch (err) {
        console.error("Error:", err);
        alert("Failed to create user.");
    }
}
