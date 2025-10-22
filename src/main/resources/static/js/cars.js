// Car management with JWT authentication
const apiBase = "http://localhost:1000/api/cars";

document.addEventListener("DOMContentLoaded", () => {
    // Check authentication before loading cars
    if (authManager.isAuthenticated()) {
        fetchCars();
    } else {
        showMessage('Please login to view cars', 'error');
        authManager.showLoginModal();
    }
});

function fetchCars() {
    if (!authManager.isAuthenticated()) {
        showMessage('Please login to view cars', 'error');
        authManager.showLoginModal();
        return;
    }

    fetch(apiBase, {
        headers: authManager.getAuthHeaders()
    })
        .then(res => {
            if (res.status === 401) {
                authManager.logout();
                showMessage('Session expired. Please login again.', 'error');
                return;
            }
            return res.json();
        })
        .then(cars => {
            if (cars) {
                const body = document.getElementById("carTableBody");
                if (body) {
                    body.innerHTML = "";
                    var counter = 0;
                    cars.forEach(car => {
                        body.innerHTML += `
                      <tr class="text-center">
                        <td class="border p-2">${++counter}</td>
                        <td class="border p-2">${car.make}</td>
                        <td class="border p-2">${car.model}</td>
                        <td class="border p-2">${car.year}</td>
                        <td class="border p-2">${car.color}</td>
                        <td class="border p-2">
                          <button onclick="openEditModal(${car.id}, '${car.make}', '${car.model}', ${car.year}, '${car.color}')" class="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600">Edit</button>
                          <button onclick="deleteCar(${car.id})" class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600">Delete</button>
                        </td>
                      </tr>`;
                    });
                }
            }
        })
        .catch(err => {
            console.error('Error fetching cars:', err);
            showMessage('Error loading cars', 'error');
        });
}

function openCreateModal() {
    if (!authManager.isAuthenticated()) {
        showMessage('Please login to add cars', 'error');
        authManager.showLoginModal();
        return;
    }

    document.getElementById("carForm").reset();
    document.getElementById("carId").value = "";
    document.getElementById("modalTitle").innerText = "Add Car";
    document.getElementById("carModal").classList.remove("hidden");
}

function openEditModal(id, make, model, year, color) {
    if (!authManager.isAuthenticated()) {
        showMessage('Please login to edit cars', 'error');
        authManager.showLoginModal();
        return;
    }

    document.getElementById("carId").value = id;
    document.getElementById("carMake").value = make;
    document.getElementById("carModel").value = model;
    document.getElementById("carYear").value = year;
    document.getElementById("carColor").value = color;
    document.getElementById("modalTitle").innerText = "Edit Car";
    document.getElementById("carModal").classList.remove("hidden");
}

function closeModal() {
    document.getElementById("carModal").classList.add("hidden");
}

function saveCar(e) {
    e.preventDefault();
    
    if (!authManager.isAuthenticated()) {
        showMessage('Please login to save cars', 'error');
        authManager.showLoginModal();
        return;
    }

    const id = document.getElementById("carId").value;
    const make = document.getElementById("carMake").value;
    const model = document.getElementById("carModel").value;
    const year = document.getElementById("carYear").value;
    const color = document.getElementById("carColor").value;

    const car = { make, model, year, color };
    const method = id ? "PUT" : "POST";
    const url = id ? `${apiBase}/${id}` : apiBase;

    fetch(url, {
        method,
        headers: authManager.getAuthHeaders(),
        body: JSON.stringify(car)
    })
        .then(res => {
            if (res.status === 401) {
                authManager.logout();
                showMessage('Session expired. Please login again.', 'error');
                return;
            }
            return res.json();
        })
        .then(data => {
            if (data) {
                closeModal();
                fetchCars();
                showMessage(id ? 'Car updated successfully!' : 'Car added successfully!', 'success');
            }
        })
        .catch(err => {
            console.error('Error saving car:', err);
            showMessage('Error saving car', 'error');
        });
}

function deleteCar(id) {
    if (!authManager.isAuthenticated()) {
        showMessage('Please login to delete cars', 'error');
        authManager.showLoginModal();
        return;
    }

    if (!confirm("Delete this car?")) return;

    fetch(`${apiBase}/${id}`, {
        method: "DELETE",
        headers: authManager.getAuthHeaders()
    })
        .then(res => {
            if (res.status === 401) {
                authManager.logout();
                showMessage('Session expired. Please login again.', 'error');
                return;
            }
            if (res.ok) {
                fetchCars();
                showMessage('Car deleted successfully!', 'success');
            } else {
                showMessage('Error deleting car', 'error');
            }
        })
        .catch(err => {
            console.error('Error deleting car:', err);
            showMessage('Error deleting car', 'error');
        });
}
