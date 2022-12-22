package de.rwth.swc.universitymanagement.request;

public class CourseRequest {
    private String id;
    private String name;
    private String instituteId;
    private Integer credits;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "CourseRequest{" +
                "courseId='" + id + '\'' +
                ", name='" + name + '\'' +
                ", instituteId='" + instituteId + '\'' +
                ", credits=" + credits +
                '}';
    }
}
