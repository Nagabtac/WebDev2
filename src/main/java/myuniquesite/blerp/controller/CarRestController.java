package myuniquesite.blerp.controller;

import myuniquesite.blerp.model.Car;
import myuniquesite.blerp.repository.CarRepository;
import myuniquesite.blerp.exception.ResourceNotFoundException;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarRestController {

    private final CarRepository carRepository;

    @Autowired
    public CarRestController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // GET all cars
    @GetMapping
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // GET one car by ID
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Integer id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + id + " not found.", id));
        return ResponseEntity.ok(car);
    }

    // POST - create new car
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Car createCar(@Valid @RequestBody Car car) {
        return carRepository.save(car);
    }

    // PUT - update car
    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Integer id, @Valid @RequestBody Car carDetails) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + id + " not found for update.", id));

        car.setLicensePlate(carDetails.getLicensePlate());
        car.setMake(carDetails.getMake());
        car.setModel(carDetails.getModel());
        car.setYear(carDetails.getYear());
        car.setColor(carDetails.getColor());
        car.setBodyType(carDetails.getBodyType());
        car.setEngineType(carDetails.getEngineType());
        // Note: transmission field removed from Car model

        return carRepository.save(car);
    }

    // DELETE car
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + id + " not found for delete.", id));
        carRepository.delete(car);
        return ResponseEntity.noContent().build();
    }
}