package de.rwth.swc.universitymanagement.controller;

import de.rwth.swc.universitymanagement.entity.Student;
import de.rwth.swc.universitymanagement.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.net.URI;

@RestController
@RequestMapping("/students")
public class StudentController {

    final private StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping("/{matriculationNumber}")
    public ResponseEntity<Student> getByMatriculationNumber(@PathVariable Integer matriculationNumber) {
        Optional<Student> optionalStudent = studentRepository.findById(matriculationNumber);
        if (optionalStudent.isPresent()) {
            return ResponseEntity.ok(optionalStudent.get());
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {

        // Better with default in the database, but I don't know how to do it with java persistence and h2
        if (student.getCredits() == null) {
            student.setCredits(0);
        }

        Student savedStudent = studentRepository.save(student);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{matriculationNumber}")
                .buildAndExpand(savedStudent.getMatriculationNumber())
                .toUri();

        return ResponseEntity.created(location).body(savedStudent);
    }

    @PutMapping("/{matriculationNumber}")
    public ResponseEntity<Student> updateByMatriculationNumber(@PathVariable Integer matriculationNumber, @RequestBody Student student) {
        Optional<Student> optionalStudent = studentRepository.findById(matriculationNumber);

        student.setMatriculationNumber(matriculationNumber);
        Student savedStudent = studentRepository.save(student);

        if (optionalStudent.isPresent()) {
            return ResponseEntity.ok(savedStudent);
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("")
                .build()
                .toUri();
        return ResponseEntity.created(location).body(savedStudent);
    }

    @DeleteMapping("/{matriculationNumber}")
    public ResponseEntity<Void> deleteByMatriculationNumber(@PathVariable Integer matriculationNumber) {
        Optional<Student> optionalStudent = studentRepository.findById(matriculationNumber);
        if (optionalStudent.isPresent()) {
            studentRepository.deleteById(matriculationNumber);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
