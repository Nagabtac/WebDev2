package com.myuniquesite.Exam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.myuniquesite.Exam.exception.ResourceNotFoundException;

@Controller
public class HomeController {

    private final EmployeeRepository employeeRepository;




    @Autowired
    public HomeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }





    @GetMapping("/")//for showing or list the data
    public String index(Model model){
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        employees.forEach(employee -> {
            System.out.println(employee.getName());
        });
        return "html/index";
    }

    @GetMapping("/create")//make a new employee
    public String create(Model model){
        model.addAttribute("employee", new Employee());
        return "html/create";
    }

    @GetMapping("/update")//update employee details
    public String update(Model model){
        return "html/update";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") int id, Model model) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        model.addAttribute("employee", employee);
        return "html/view";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") int id, Model model) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        model.addAttribute("employee", employee);
        return "html/update";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable("id") int id,
                             @Valid @ModelAttribute("employee") Employee form,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", form);
            return "html/update";
        }
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        employee.setName(form.getName());
        employee.setEmail(form.getEmail());
        employeeRepository.save(employee);
        return "redirect:/";
    }

    @PostMapping("/create")
    public String createSubmit(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", employee);
            return "html/create";
        }
        employeeRepository.save(employee);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        employeeRepository.delete(employee);
        return "redirect:/";
    }
}
/*
* */