package de.rwth.swc.universitymanagement.repository;

import de.rwth.swc.universitymanagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}
