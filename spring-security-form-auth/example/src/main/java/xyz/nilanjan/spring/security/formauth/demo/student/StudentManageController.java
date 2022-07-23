package xyz.nilanjan.spring.security.formauth.demo.student;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/management/students")
public class StudentManageController {
    private static final List<Student> STUDENTS = new ArrayList<>(
            List.of(
                    new Student(1, "Nilanjan Deb"),
                    new Student(2, "John Doe"),
                    new Student(3, "Priyank Piyush")
            )
    );

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMIN_TRAINEE')")
    public List<Student> getStudents() {
        return STUDENTS;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student) {
        System.out.printf("Register new student: %s", student);
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(
            @PathVariable("studentId") Integer studentId,
            @RequestBody Student student
    ) {
        System.out.printf("Student %s, StudentId %s%n", student, studentId);
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        System.out.printf("Removing Student with id %s", studentId);
    }
}
