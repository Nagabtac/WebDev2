package myuniquesite.blerp.controller;

import myuniquesite.blerp.model.Car;
import myuniquesite.blerp.service.CarService;
import myuniquesite.blerp.exception.ResourceNotFoundException;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*") // ✅ Allow requests from all origins (OK for dev)
public class CarRestController {

    private final CarService carService;

    @Autowired
    public CarRestController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = carService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + id + " not found.", id));
        return ResponseEntity.ok(car);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Car createCar(@Valid @RequestBody Car car) {
        return carService.save(car);
    }

    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @Valid @RequestBody Car carDetails) {
        Car car = carService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + id + " not found for update.", id));

        car.setLicensePlate(carDetails.getLicensePlate());
        car.setMake(carDetails.getMake());
        car.setModel(carDetails.getModel());
        car.setYear(carDetails.getYear());
        car.setColor(carDetails.getColor());
        car.setBodyType(carDetails.getBodyType());
        car.setEngineType(carDetails.getEngineType());

        return carService.save(car);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + id + " not found for delete.", id));

        carService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
