package xyz.nilanjan.restwithjpa.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import xyz.nilanjan.restwithjpa.demo.domain.Student;
import xyz.nilanjan.restwithjpa.demo.service.StudentService;

import java.time.LocalDate;
import java.time.Month;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentService studentService) {
		return args -> {
			studentService.addNewStudent(
					new Student(
							"Nilanjan",
							"Deb",
							"nil@gmail.com",
							LocalDate.of(2000, Month.JULY, 3)
					)
			);
		};
	}
}
