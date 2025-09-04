package myuniquesite.blerp.utils;

import myuniquesite.blerp.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbTester implements CommandLineRunner {

    private final CarRepository carRepository;

    public DbTester(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        Car car = new Car();
//        car.setModel("Vios");
//        car.setYear(2017);
//        car.setMake("Toyota");
//        car.setColor("Grey");
//        carRepository.save(car);
//
//        carRepository.findAll().forEach(carRow -> {
//            System.out.println(carRow.getMake() + " " + carRow.getModel() + " " + carRow.getColor());
//        });

    }
}