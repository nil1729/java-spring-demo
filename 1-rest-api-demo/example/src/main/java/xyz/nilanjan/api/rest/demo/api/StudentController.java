package xyz.nilanjan.api.rest.demo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.nilanjan.api.rest.demo.domain.Student;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private Long currentId = 2L;
    private final List<Student> students = new ArrayList<>(
            List.of(
                    new Student(
                            1L,
                            "Nilanjan",
                            "Deb",
                            "nil@gmail.com",
                            LocalDate.of(2000, Month.JULY, 3),
                            22
                    )
            )
    );

    private Student addStudentToList(Student student) {
        Student newStudent = new Student(
                this.currentId,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getDob(),
                student.getAge()
        );
        this.students.add(newStudent);
        this.currentId++;
        return newStudent;
    }

    private Student findStudentByEmail(String email) {
        for (int i = 0; i < this.students.size(); i++) {
            if (this.students.get(i).getEmail().equals(email)) {
                return this.students.get(i);
            }
        }
        return null;
    }

    private Student findStudentById(Long student_id) {
        if (student_id > this.students.size()) return null;
        return this.students.get((int) (student_id - 1L));
    }


    private void updateStudentById(
            Student updatedStudent,
            Long student_id
    ) {
        this.students.set((int)(student_id - 1L), updatedStudent);
    }

    private void removeStudentById(Long student_id) {
        this.students.remove((int) (student_id - 1));
    }


    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok().body(students);
    }

    @PostMapping
    public ResponseEntity<?> addNewStudent(
            @RequestBody StudentRequestBody studentRequestBody
    ) {

        Student studentWithSameEmail =
                this.findStudentByEmail(studentRequestBody.getEmail());

        if (studentWithSameEmail == null) {
            Student newStudent = this.addStudentToList(
                    new Student(
                            studentRequestBody.getFirstName(),
                            studentRequestBody.getLastName(),
                            studentRequestBody.getEmail(),
                            studentRequestBody.getDob(),
                            studentRequestBody.getAge()
                    )
            );
            return ResponseEntity.ok().body(newStudent);
        } else {
            return ResponseEntity.badRequest().body("Email already exists");
        }
    }

    @PutMapping("/{student_id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable("student_id") Long student_id,
            @RequestBody StudentRequestBody studentRequestBody
    ) {
        Student requestedStudent = this.findStudentById(student_id);

        if (requestedStudent != null) {
            Student studentWithSameEmail =
                    this.findStudentByEmail(studentRequestBody.getEmail());

            if (studentWithSameEmail == null ||
                    studentWithSameEmail.getId() == requestedStudent.getId()) {

                Student updatedStudent = new Student(
                        requestedStudent.getId(),
                        studentRequestBody.getFirstName(),
                        studentRequestBody.getLastName(),
                        studentRequestBody.getEmail(),
                        studentRequestBody.getDob(),
                        studentRequestBody.getAge()
                );

                this.updateStudentById(updatedStudent, student_id);
                return ResponseEntity.ok().body(updatedStudent);
            } else {
                return ResponseEntity.badRequest().body("Email already exists");
            }
        } else {
            return ResponseEntity.badRequest().body("Requested student not found");
        }
    }

    @DeleteMapping("/{student_id}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable("student_id") Long student_id
    ) {
        Student requestedStudent = this.findStudentById(student_id);
        if(requestedStudent != null) {
            this.removeStudentById(student_id);
            return ResponseEntity.ok().body("Student deleted");
        } else {
            return ResponseEntity.badRequest().body("Requested student not found");
        }
    }
}


class StudentRequestBody {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;

    public StudentRequestBody(
            String firstName,
            String lastName,
            String email,
            LocalDate dob
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "StudentRequestBody{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                '}';
    }
}

