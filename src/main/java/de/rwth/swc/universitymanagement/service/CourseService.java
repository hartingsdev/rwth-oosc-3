package de.rwth.swc.universitymanagement.service;

import de.rwth.swc.universitymanagement.entity.Course;
import de.rwth.swc.universitymanagement.entity.Institute;
import de.rwth.swc.universitymanagement.request.CourseRequest;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    public static Course transformCourse(CourseRequest courseRequest, Institute institute) {
        Course course = new Course();

        course.setId(courseRequest.getId());
        course.setName(courseRequest.getName());
        course.setCredits(courseRequest.getCredits());
        course.setInstitute(institute);

        return course;
    }
}
