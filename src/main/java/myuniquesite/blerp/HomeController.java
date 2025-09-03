package myuniquesite.blerp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private CsvService csvService;

    // Display all cars on the main page
    @GetMapping("/")
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
        model.addAttribute("car", new Car());
        return "add";
    }

    // Handle form submission to add new car
    @PostMapping("/add")
    public String addCar(@ModelAttribute Car car, RedirectAttributes redirectAttributes) {
        System.out.println("Saving car: " + car.getMake() + " " + car.getModel());
        Car savedCar = carRepository.save(car);
        System.out.println("Car saved with ID: " + savedCar.getId());
        
        // Export to CSV after adding
        List<Car> cars = carRepository.findAll();
        csvService.exportToCsv(cars);
        redirectAttributes.addFlashAttribute("message", "Car added successfully! CSV updated automatically.");
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
    public String updateCar(@PathVariable int id, @ModelAttribute Car car, RedirectAttributes redirectAttributes) {
        car.setId(id);
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


}
