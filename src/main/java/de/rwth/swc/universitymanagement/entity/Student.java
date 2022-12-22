package de.rwth.swc.universitymanagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matriculationNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private Integer credits;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "student_courses",
            joinColumns = @JoinColumn(name = "student_matriculationNumber"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();

    public Integer getMatriculationNumber() {
        return matriculationNumber;
    }

    public void setMatriculationNumber(Integer matriculationNumber) {
        this.matriculationNumber = matriculationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "matriculationNumber=" + matriculationNumber +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", credits=" + credits +
                ", courses=" + courses +
                '}';
    }
}
