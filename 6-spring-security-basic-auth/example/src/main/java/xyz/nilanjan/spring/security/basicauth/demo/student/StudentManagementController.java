package xyz.nilanjan.spring.security.basicauth.demo.student;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/management/students")
public class StudentManagementController {
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

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMIN_TRAINEE')")
    public List<Student> getStudents() {
        System.out.println("StudentManagementController.getStudents");
        return STUDENTS;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void addNewStudent(@RequestBody Student student) {
        System.out.println("StudentManagementController.addNewStudent");
        System.out.println("student = " + student);
    }

    @PutMapping("{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        System.out.println("StudentManagementController.updateStudent");
        System.out.println("studentId = " + studentId + ", student = " + student);
    }

    @DeleteMapping("{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        System.out.println("StudentManagementController.deleteStudent");
        System.out.println("studentId = " + studentId);
    }
}
