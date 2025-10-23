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
import myuniquesite.blerp.service.UserService;
import myuniquesite.blerp.exception.ResourceNotFoundException;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CsvService csvService;
    
    @Autowired
    private UserService userService;

    @GetMapping({"/", "/index"})
    public String home(Model model) {
        List<Car> cars = carRepository.findAll();
        model.addAttribute("cars", cars);
        return "index";
    }
    
    // ✅ Added mapping to serve the login page
    @GetMapping("/login")
    public String login() {
        return "login"; // Renders login.html
    }
    
    // ✅ Added mapping to serve the registration page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Object());
        return "register";
    }

    // ✅ This handles form submission from the server-rendered register.html page
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
        return "redirect:/login";
    }

    // --- Other methods for server-side rendered pages remain unchanged ---

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("car", new CarDTO());
        return "add";
    }

    @PostMapping("/add")
    public String addCar(@Valid @ModelAttribute("car") CarDTO carDTO,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "add";
        }
        Car car = new Car();
        car.setMake(carDTO.getMake());
        car.setModel(carDTO.getModel());
        car.setYear(carDTO.getYear());
        car.setColor(carDTO.getColor());
        car.setBodyType(carDTO.getBodyType());
        car.setEngineType(carDTO.getEngineType());
        car.setLicensePlate(carDTO.getLicensePlate());
        carRepository.save(car);
        csvService.exportToCsv(carRepository.findAll());
        redirectAttributes.addFlashAttribute("message", "Car added successfully!");
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));
        model.addAttribute("car", car);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable Long id,
                            @Valid @ModelAttribute("car") Car car,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        car.setId(id);
        carRepository.save(car);
        csvService.exportToCsv(carRepository.findAll());
        redirectAttributes.addFlashAttribute("message", "Car updated successfully!");
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        carRepository.deleteById(id);
        csvService.exportToCsv(carRepository.findAll());
        redirectAttributes.addFlashAttribute("message", "Car deleted successfully!");
        return "redirect:/";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));
        model.addAttribute("car", car);
        return "view";
    }
}