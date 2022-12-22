package de.rwth.swc.universitymanagement.repository;

import de.rwth.swc.universitymanagement.entity.Institute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstituteRepository extends JpaRepository<Institute, String> {
}
