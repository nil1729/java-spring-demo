package xyz.nilanjan.spring.exception.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nilanjan.spring.exception.demo.exception.ApiRequestException;

import java.util.List;

@Service
public class StudentService {
    private final StudentDataAccessService studentDataAccessService;

    @Autowired
    public StudentService(StudentDataAccessService studentDataAccessService) {
        this.studentDataAccessService = studentDataAccessService;
    }

    public List<Student> getStudents() {
        return studentDataAccessService.getStudents();
    }

    public Student getStudentById(Long studentId) {
        return studentDataAccessService
                .getStudents()
                .stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .orElseThrow(() ->
                        new ApiRequestException(String.format("Student with id %s not found", studentId))
                );
    }
}
