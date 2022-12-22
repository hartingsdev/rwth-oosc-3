package de.rwth.swc.universitymanagement.controller;

import de.rwth.swc.universitymanagement.entity.Course;
import de.rwth.swc.universitymanagement.entity.Institute;
import de.rwth.swc.universitymanagement.repository.CourseRepository;
import de.rwth.swc.universitymanagement.repository.InstituteRepository;
import de.rwth.swc.universitymanagement.request.CourseRequest;
import de.rwth.swc.universitymanagement.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    final private CourseRepository courseRepository;
    final private InstituteRepository instituteRepository;

    public CourseController(CourseRepository courseRepository, InstituteRepository instituteRepository) {
        this.courseRepository = courseRepository;
        this.instituteRepository = instituteRepository;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getByID(@PathVariable String id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            return ResponseEntity.ok(optionalCourse.get());
        }

        return ResponseEntity.notFound().build();

    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseRequest courseRequest) {
        Optional<Course> optionalCourse = courseRepository.findById(courseRequest.getId());
        if (optionalCourse.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<Institute> optionalInstitute = instituteRepository.findById(courseRequest.getInstituteId());
        if (optionalInstitute.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Course savedCourse = courseRepository.save(CourseService.transformCourse(courseRequest, optionalInstitute.get()));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCourse.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateById(@PathVariable String id, @RequestBody CourseRequest courseRequest) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        Optional<Institute> optionalInstitute = instituteRepository.findById(courseRequest.getInstituteId());

        if (optionalInstitute.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        courseRequest.setId(id);
        Course savedCourse = courseRepository.save(CourseService.transformCourse(courseRequest, optionalInstitute.get()));

        if (optionalCourse.isPresent()) {
            return ResponseEntity.ok(savedCourse);
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("")
                .build()
                .toUri();

        return ResponseEntity.created(location).body(savedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByMatriculationNumber(@PathVariable String id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            courseRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
