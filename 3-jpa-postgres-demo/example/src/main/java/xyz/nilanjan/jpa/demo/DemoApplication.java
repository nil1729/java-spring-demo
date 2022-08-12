package xyz.nilanjan.jpa.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import xyz.nilanjan.jpa.demo.domain.Student;
import xyz.nilanjan.jpa.demo.repository.StudentRepository;

import java.time.LocalDate;
import java.time.Month;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Student nilanjan = new Student(
                    "Nilanjan",
                    "Deb",
                    "nil@gmail.com",
                    LocalDate.of(2000, Month.JULY, 3),
                    22
            );

            studentRepository.save(nilanjan);
        };
    }
}
