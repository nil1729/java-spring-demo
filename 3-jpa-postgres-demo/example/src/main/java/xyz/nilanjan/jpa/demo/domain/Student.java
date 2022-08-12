package xyz.nilanjan.jpa.demo.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "Student")
@Table(
        name = "student",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "student_email_unique",
                        columnNames = {"email"}
                )
        }
)
public class Student {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "student_sequence"
    )
    @Column(
            name = "id",
            updatable = false,
            nullable = false
    )
    private Long id;
    @Column(
            name = "first_name",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String firstName;
    @Column(
            name = "last_name",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String lastName;
    @Column(
            name = "email",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String email;
    @Column(
            name = "dob",
            nullable = false
    )
    private LocalDate dob;
    @Column(
            name = "age",
            nullable = false
    )
    private Integer age;

    public Student() {
    }

    public Student(
            Long id,
            String firstName,
            String lastName,
            String email,
            LocalDate dob,
            Integer age
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.age = age;
    }

    public Student(
            String firstName,
            String lastName,
            String email,
            LocalDate dob,
            Integer age
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
