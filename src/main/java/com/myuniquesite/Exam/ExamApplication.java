package com.myuniquesite.Exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.myuniquesite.Exam.service.UserService;
import com.myuniquesite.Exam.Employee;
import com.myuniquesite.Exam.EmployeeRepository;


@SpringBootApplication
public class ExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamApplication.class, args);
	}

    @Bean
    CommandLineRunner loadSampleData(EmployeeRepository employeeRepository, UserService userService) {
        return args -> {
         
            if (employeeRepository.count() == 0) {
                Employee employee = new Employee();
                employee.setName("Darwin");
                employee.setEmail("darwin@gmail.com");
                employeeRepository.save(employee);
            }
            
           
            try {
                userService.registerUser("admin", "password");
                System.out.println("Default admin user created successfully");
            } catch (RuntimeException e) {
                if (e.getMessage().contains("Username already exists")) {
                    System.out.println("Default admin user already exists");
                } else {
                    System.out.println("Error creating default user: " + e.getMessage());
                }
            }
        };
    }

}
