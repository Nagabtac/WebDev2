package myuniquesite.blerp.dto;

import myuniquesite.blerp.dto.*;
import myuniquesite.blerp.exception.ResourceNotFoundException;
import myuniquesite.blerp.model.Car;
import myuniquesite.blerp.repository.CarRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    CarRepository carRepository;

    public HomeController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("/car-ajax")
    public String carAjax(Model model) {
        return "car-ajax";
    }
    @GetMapping("/")
    public String index(Model model){
        List<Car> cars = carRepository.findAll();
        model.addAttribute("cars",carRepository.findAll());
        cars.forEach(car -> {
            System.out.println(car.getMake());
        });
        return "index";
    }

    @GetMapping("/create")
    public String create(Model model){
        CarDTO car = new CarDTO();
        model.addAttribute("car",car);
        return "create";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("car") @Valid CarDTO car, BindingResult result, Model model){

        if(result.hasErrors()){
            model.addAttribute("car",car);
            return "create";
        }

        Car newCar = new Car();
        newCar.setMake(car.getMake());
        newCar.setModel(car.getModel());
        newCar.setYear(car.getYear());
        newCar.setColor(car.getColor());
        carRepository.save(newCar);

        return "redirect:/";


    }

    @GetMapping("/show")
    public String show(@RequestParam int id, Model model){
        Car car = carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Car", id));
        System.out.println("reached this point");

        model.addAttribute("car",car);
        return "show";
    }
}
