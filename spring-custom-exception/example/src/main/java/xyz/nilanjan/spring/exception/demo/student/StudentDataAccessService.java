package xyz.nilanjan.spring.exception.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.nilanjan.spring.exception.demo.datastore.FakeStudentDataStore;

import java.util.List;

@Repository
public class StudentDataAccessService {
    private final FakeStudentDataStore fakeStudentDataStore;

    @Autowired
    public StudentDataAccessService(FakeStudentDataStore fakeStudentDataStore) {
        this.fakeStudentDataStore = fakeStudentDataStore;
    }

    public List<Student> getStudents() {
        return fakeStudentDataStore.getStudents();
    }
}
