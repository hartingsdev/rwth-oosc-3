package de.rwth.swc.universitymanagement.repository;

import de.rwth.swc.universitymanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository extends JpaRepository<Student, Integer> {
}
