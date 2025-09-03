package myuniquesite.blerp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import myuniquesite.blerp.model.Car;
import myuniquesite.blerp.repository.CarRepository;

import java.util.List;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private CsvService csvService;

    @Override
    public void run(String... args) throws Exception {
        // Check if database is empty
        if (carRepository.count() == 0) {
            // Import data from CSV if database is empty
            List<Car> cars = csvService.importFromCsv();
            if (!cars.isEmpty()) {
                for (Car car : cars) {
                    // Reset ID to let JPA generate new ones
                    car.setId(0);
                    carRepository.save(car);
                }
                System.out.println("Imported " + cars.size() + " cars from CSV to database");
            }
        }
    }
}
