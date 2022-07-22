package xyz.nilanjan.restwithjpa.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import xyz.nilanjan.restwithjpa.demo.domain.Student;
import xyz.nilanjan.restwithjpa.demo.service.StudentService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudent() {
        return ResponseEntity.ok().body(studentService.getStudents());
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/v1/students")
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(studentService.addNewStudent(student));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Optional<Student>> getStudentById(
            @PathVariable("studentId") Long studentId
    ) {
        return ResponseEntity.ok().body(studentService.getStudentById(studentId));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<String> updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestBody Student student
    ) {
        studentService.updateExistingStudent(student, studentId);
        return ResponseEntity.ok().body("student updated");
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> removeStudent(
            @PathVariable("studentId") Long studentId
    ) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok().body("student removed");
    }
}
