package xyz.nilanjan.jpa.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nilanjan.jpa.demo.domain.Student;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, Long> {

}
