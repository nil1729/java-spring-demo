package xyz.nilanjan.spring.exception.demo.datastore;

import org.springframework.stereotype.Repository;
import xyz.nilanjan.spring.exception.demo.student.Student;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FakeStudentDataStore {
    private final static List<Student> STUDENTS = new ArrayList<>();

    static {
        STUDENTS.add(
                new Student(1L, "Nilanjan Deb")
        );
        STUDENTS.add(
                new Student(2L, "John Doe")
        );
        STUDENTS.add(
                new Student(3L, "Priyank Piyush")
        );
    }

    public List<Student> getStudents() {
        return STUDENTS;
    }
}
