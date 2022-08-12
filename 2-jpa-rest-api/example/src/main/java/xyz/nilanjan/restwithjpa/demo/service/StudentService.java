package xyz.nilanjan.restwithjpa.demo.service;

import xyz.nilanjan.restwithjpa.demo.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student addNewStudent(Student student);

    List<Student> getStudents();

    Optional<Student> getStudentById(Long studentId);

    void updateExistingStudent(Student student, Long student_id);

    void deleteStudent(Long student_id);
}
