package myuniquesite.blerp.service;

import myuniquesite.blerp.model.Car;
import myuniquesite.blerp.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes database data on application startup.
 * The manual setId() call has been removed to prevent compilation errors
 * and to allow the database to auto-generate the primary key.
 */
@Component
public class DataInitializationService implements CommandLineRunner {

    private final CarRepository carRepository;

    public DataInitializationService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only run initialization if the database is empty
        if (carRepository.count() == 0) {
            System.out.println("Seeding initial Car data...");

            // Create some sample cars
            Car car1 = new Car();
            // FIX: Removed the car1.setId(1L) call. 
            // The ID will now be generated automatically by the database upon saving.
            car1.setMake("Toyota");
            car1.setModel("Corolla");
            car1.setYear(2020);
            car1.setColor("Red");
            car1.setBodyType("Sedan");
            car1.setEngineType("Gasoline");
            carRepository.save(car1);

            Car car2 = new Car();
            car2.setMake("BMW");
            car2.setModel("coupe");
            car2.setYear(2024);
            car2.setColor("Black");
            car2.setBodyType("Coupe");
            car2.setEngineType("Diesel");
            carRepository.save(car2);

            System.out.println("Initial car data seeded successfully.");
        }
    }
}

