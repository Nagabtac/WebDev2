package com.myuniquesite.Exam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        return "index.html";
    }

    @GetMapping("/create")//make a new employee
    public String create(Model model){
        return "create";
    }

    @GetMapping("/update")//update employee details
    public String update(Model model){
        return "update";
    }
}
/*
* */