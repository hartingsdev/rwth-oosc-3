package de.rwth.swc.universitymanagement.controller;

import de.rwth.swc.universitymanagement.entity.Institute;
import de.rwth.swc.universitymanagement.repository.InstituteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/institutes")
public class InstituteController {

    final private InstituteRepository instituteRepository;

    public InstituteController(InstituteRepository instituteRepository) {
        this.instituteRepository = instituteRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<Institute>> get_all() {
        return ResponseEntity.ok(instituteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institute> getInstituteByID(@PathVariable String id) {
        Optional<Institute> optionalInstitute = instituteRepository.findById(id);
        if (optionalInstitute.isPresent()) {
            return ResponseEntity.ok(optionalInstitute.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Institute> createInstitute(@RequestBody Institute institute) {
        Institute savedInstitute = instituteRepository.save(institute);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedInstitute.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedInstitute);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Institute> updateByID(@PathVariable String id, @RequestBody Institute institute) {
        Optional<Institute> optionalStudent = instituteRepository.findById(id);

        institute.setId(id);
        Institute savedInstitute = instituteRepository.save(institute);

        if (optionalStudent.isPresent()) {
            return ResponseEntity.ok(savedInstitute);
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("")
                .build()
                .toUri();
        return ResponseEntity.created(location).body(savedInstitute);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByMatriculationNumber(@PathVariable String id) {
        Optional<Institute> optionalStudent = instituteRepository.findById(id);
        if (optionalStudent.isPresent()) {
            instituteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
