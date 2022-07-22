package xyz.nilanjan.restwithjpa.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.nilanjan.restwithjpa.demo.domain.Student;
import xyz.nilanjan.restwithjpa.demo.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student addNewStudent(Student student) {
        Optional<Student> studentWithSameEmail =
                studentRepository.findStudentByEmail(student.getEmail());

        if (studentWithSameEmail.isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        return studentRepository.save(student);
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentById(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);

        if (student.isEmpty()) {
            throw new IllegalStateException("student with id " + studentId + " not found");
        }

        return student;
    }

    @Override
    @Transactional
    public void updateExistingStudent(Student student, Long studentId) {
        Student requestedStudent = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new IllegalStateException("student with id " + studentId + " not found")
                );

        if (student.getEmail() != null) {
            Optional<Student> studentWithSameEmail =
                    studentRepository.findStudentByEmail(student.getEmail());

            if (studentWithSameEmail.isPresent() &&
                    !studentWithSameEmail.get().getId().equals(studentId)) {
                throw new IllegalStateException("Email already exists");
            }

            requestedStudent.setEmail(student.getEmail());
        }

        if (student.getFirstName() != null &&
                !student.getFirstName().equals(requestedStudent.getFirstName())) {
            requestedStudent.setFirstName(student.getFirstName());
        }

        if (student.getLastName() != null &&
                !student.getLastName().equals(requestedStudent.getLastName())) {
            requestedStudent.setLastName(student.getLastName());
        }

        if (student.getDob() != null &&
                !student.getDob().equals(requestedStudent.getDob())) {
            requestedStudent.setDob(student.getDob());
        }
    }

    @Override
    public void deleteStudent(Long studentId) {
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId);
        } else {
            throw new IllegalStateException("student with id " + studentId + " not found");
        }
    }
}
