package myuniquesite.blerp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import myuniquesite.blerp.model.Car;
import myuniquesite.blerp.dto.CarDTO;
import myuniquesite.blerp.repository.CarRepository;
import myuniquesite.blerp.service.CsvService;
import myuniquesite.blerp.exception.ResourceNotFoundException;


import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private CsvService csvService;

    // Display all cars on the main page
    @GetMapping({"/", "/index"})
    public String home(Model model) {
        List<Car> cars = carRepository.findAll();
        System.out.println("Found " + cars.size() + " cars in database");
        for (Car car : cars) {
            System.out.println("Car: " + car.getMake() + " " + car.getModel() + " (ID: " + car.getId() + ")");
        }
        model.addAttribute("cars", cars);
        return "index";
    }

    // Show form to add new car
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("car", new CarDTO());
        return "add";
    }

    // Show form to create new car
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("car", new CarDTO());
        return "create";
    }

    // Handle form submission to add new car
    @PostMapping("/add")
    public String addCar(@Valid @ModelAttribute("car") CarDTO carDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        System.out.println("=== VALIDATION DEBUG ===");
        System.out.println("Make: '" + carDTO.getMake() + "'");
        System.out.println("Model: '" + carDTO.getModel() + "'");
        System.out.println("Year: " + carDTO.getYear());
        System.out.println("Color: '" + carDTO.getColor() + "'");
        System.out.println("Validation errors: " + bindingResult.hasErrors());
        if (bindingResult.hasErrors()) {
            System.out.println("Errors: " + bindingResult.getAllErrors());
            for (var error : bindingResult.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            return "add";
        }
        
        // Convert DTO to Entity
        Car car = new Car();
        car.setMake(carDTO.getMake());
        car.setModel(carDTO.getModel());
        car.setYear(carDTO.getYear());
        car.setColor(carDTO.getColor());
        car.setBodyType(carDTO.getBodyType());
        car.setEngineType(carDTO.getEngineType());
        car.setLicensePlate(carDTO.getLicensePlate());
        
        System.out.println("Saving car: " + car.getMake() + " " + car.getModel());
        Car savedCar = carRepository.save(car);
        System.out.println("Car saved with ID: " + savedCar.getId());
        
        // Export to CSV after adding
        List<Car> cars = carRepository.findAll();
        csvService.exportToCsv(cars);
        redirectAttributes.addFlashAttribute("message", "Car added successfully! CSV updated automatically.");
        return "redirect:/";
    }

    // Handle form submission to create new car
    @PostMapping("/create")
    public String createCar(@Valid @ModelAttribute("car") CarDTO carDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        System.out.println("=== VALIDATION DEBUG ===");
        System.out.println("Make: '" + carDTO.getMake() + "'");
        System.out.println("Model: '" + carDTO.getModel() + "'");
        System.out.println("Year: " + carDTO.getYear());
        System.out.println("Color: '" + carDTO.getColor() + "'");
        System.out.println("Validation errors: " + bindingResult.hasErrors());
        if (bindingResult.hasErrors()) {
            System.out.println("Errors: " + bindingResult.getAllErrors());
            for (var error : bindingResult.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            return "create";
        }
        
        // Convert DTO to Entity
        Car car = new Car();
        car.setMake(carDTO.getMake());
        car.setModel(carDTO.getModel());
        car.setYear(carDTO.getYear());
        car.setColor(carDTO.getColor());
        car.setBodyType(carDTO.getBodyType());
        car.setEngineType(carDTO.getEngineType());
        car.setLicensePlate(carDTO.getLicensePlate());
        
        System.out.println("Saving car: " + car.getMake() + " " + car.getModel());
        Car savedCar = carRepository.save(car);
        System.out.println("Car saved with ID: " + savedCar.getId());
        
        // Export to CSV after adding
        List<Car> cars = carRepository.findAll();
        csvService.exportToCsv(cars);
        redirectAttributes.addFlashAttribute("message", "Car created successfully! CSV updated automatically.");
        return "redirect:/";
    }

    // Show form to edit car
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Car car = carRepository.findById(id).orElse(null);
        model.addAttribute("car", car);
        return "edit";
    }

    // Handle form submission to update car
    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable int id, @Valid @ModelAttribute("car") Car car, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        car.setId((long) id);
        carRepository.save(car);
        // Export to CSV after updating
        List<Car> cars = carRepository.findAll();
        csvService.exportToCsv(cars);
        redirectAttributes.addFlashAttribute("message", "Car updated successfully! CSV updated automatically.");
        return "redirect:/";
    }

    // Delete car
    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        carRepository.deleteById(id);
        // Export to CSV after deleting
        List<Car> cars = carRepository.findAll();
        csvService.exportToCsv(cars);
        redirectAttributes.addFlashAttribute("message", "Car deleted successfully! CSV updated automatically.");
        return "redirect:/";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id, Model model){
        Car car = carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Car", id));
        model.addAttribute("car", car);
        return "view";
    }

}
