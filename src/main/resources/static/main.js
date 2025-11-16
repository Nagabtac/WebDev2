const apiBase = "http://localhost:1000/api/cars";

document.addEventListener("DOMContentLoaded", fetchCars);

function fetchCars() {
    fetch(apiBase)
        .then(res => res.json())
        .then(cars => {
            const body = document.getElementById("carTableBody");
            body.innerHTML = "";
            var counter = 0;
            cars.forEach(car => {
                body.innerHTML += `
            <tr class="text-center odd:bg-[#f8f8f8] even:bg-[#e0e0e0] hover:bg-[#d0d0d0] transition text-black">
                <td class="border border-black p-2">${++counter}</td>
                <td class="border border-black px-3 py-2">${car.make}</td>
                <td class="border border-black px-3 py-2">${car.model}</td>
                <td class="border border-black px-3 py-2">${car.year}</td>
                <td class="border border-black px-3 py-2">${car.color}</td>
                <td class="border border-black px-3 py-2">${car.bodyType}</td>
                <td class="border border-black px-3 py-2">${car.engineType}</td>
                <td class="border border-black px-3 py-2">${car.licensePlate}</td>
                <td class="border border-black p-2 space-x-2">
                <button onclick="openEditModal(${car.id}, '${car.make}', '${car.model}', ${car.year}, '${car.color}', '${car.bodyType}', '${car.engineType}', '${car.licensePlate}')" 
                    class="bg-white text-black border border-black px-3 py-1 rounded font-semibold hover:bg-black hover:text-white transition">
                    Edit
                </button>
                <button onclick="deleteCar(${car.id})" 
                    class="bg-white text-black border border-black px-3 py-1 rounded font-semibold hover:bg-black hover:text-white transition">
                    Delete
                </button>
                </td>
            </tr>`;
            });
        })
        .catch(err => console.error(err));
}

function openCreateModal() {
    document.getElementById("carForm").reset();
    document.getElementById("carId").value = "";
    document.getElementById("modalTitle").innerText = "Add Car";
    document.getElementById("carModal").classList.remove("hidden");
}

function openEditModal(id, make, model, year, color, bodyType, engineType, licensePlate) {
    document.getElementById("carId").value = id;
    document.getElementById("carMake").value = make;
    document.getElementById("carModel").value = model;
    document.getElementById("carYear").value = year;
    document.getElementById("carColor").value = color;
    document.getElementById("carBodyType").value = bodyType;
    document.getElementById("carEngineType").value = engineType;
    document.getElementById("carLicensePlate").value = licensePlate;
    document.getElementById("modalTitle").innerText = "Edit Car";
    document.getElementById("carModal").classList.remove("hidden");
}

function closeModal() {
    document.getElementById("carModal").classList.add("hidden");
}

function saveCar(e) {
    e.preventDefault();
    const id = document.getElementById("carId").value;
    const make = document.getElementById("carMake").value;
    const model = document.getElementById("carModel").value;
    const year = document.getElementById("carYear").value;
    const color = document.getElementById("carColor").value;
    const bodyType = document.getElementById("carBodyType").value;
    const engineType = document.getElementById("carEngineType").value;
    const licensePlate = document.getElementById("carLicensePlate").value;

    const car = { make, model, year, color, bodyType, engineType, licensePlate };
    const method = id ? "PUT" : "POST";
    const url = id ? `${apiBase}/${id}` : apiBase;

    fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(car)
    })
        .then(res => res.json())
        .then(() => {
            closeModal();
            fetchCars();
        })
        .catch(err => console.error(err));
}

function deleteCar(id) {
    if (!confirm("Delete this car?")) return;
    fetch(`${apiBase}/${id}`, { method: "DELETE" })
        .then(() => fetchCars())
        .catch(err => console.error(err));
}

// Additional Fetch API Examples
// Basic GET request example
fetch("https://your-api.com/resource")
    .then(response => response.json())   // Parse JSON data
    .then(data => console.log(data))     // Use the data
    .catch(error => console.error("Error:", error));

// POST request example - Create new car
const newCar = {
    make: "Ford",
    model: "Focus",
    year: 2022,
    color: "Black",
    bodyType: "Sedan",
    engineType: "Gasoline",
    licensePlate: "ABC123"
};

fetch("http://localhost:1000/api/cars", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(newCar)
})
    .then(res => res.json())
    .then(data => console.log("Car created:", data))
    .catch(error => console.error("Error creating car:", error));

// PUT request example - Update existing car
const updatedCar = {
    make: "Honda",
    model: "Civic",
    year: 2023,
    color: "White",
    bodyType: "Sedan",
    engineType: "Hybrid",
    licensePlate: "XYZ789"
};

fetch("http://localhost:1000/api/cars/2", {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(updatedCar)
})
    .then(res => res.json())
    .then(data => console.log("Car updated:", data))
    .catch(error => console.error("Error updating car:", error));

// DELETE request example - Delete car
fetch("http://localhost:1000/api/cars/2", {
    method: "DELETE"
})
    .then(() => console.log("Car deleted"))
    .catch(error => console.error("Error deleting car:", error));