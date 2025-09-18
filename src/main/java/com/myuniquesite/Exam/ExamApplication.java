package com.myuniquesite.Exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamApplication.class, args);
	}

    @Bean
    CommandLineRunner loadSampleEmployees(EmployeeRepository employeeRepository) {
        return args -> {
            if (employeeRepository.count() == 0) {
                Employee employee = new Employee();
                employee.setName("Darwin");
                employee.setEmail("darwin@gmail.com");
                employeeRepository.save(employee);
            }
        };
    }

}
