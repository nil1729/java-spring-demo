package xyz.nilanjan.spring.security.basicauth.demo.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private static final List<Student> STUDENTS = new ArrayList<>(
            List.of(
                    new Student(
                            1,
                            "Nil Deb"
                    ),
                    new Student(
                            2,
                            "John Doe"
                    ),
                    new Student(
                            3,
                            "Tom Redis"
                    )
            )
    );

    @GetMapping("/{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId) {
        return STUDENTS
                .stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .orElseThrow(()->
                        new IllegalStateException("Student with id " + studentId + " doest not exist")
                );
    }
}
