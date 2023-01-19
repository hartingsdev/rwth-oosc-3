package de.rwth.swc.universitymanagement;

import io.restassured.RestAssured;
import org.apache.http.HttpHeaders;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseRegistrationTests extends AbstractDataTests {

    @Test
    void testCourseRegistration() throws JSONException {
        createInstitute("insti", "mailaddress");
        createCourse("sqa", "Software Quality Assurance", "insti", 6);
        String studentLocation = createStudent("Max", "Mustermann")
                .extract().response().getHeader(HttpHeaders.LOCATION);

        RestAssured.post(studentLocation+"/courses/register/sqa")
                .then()
                .statusCode(200);

        RestAssured.get(studentLocation+"/courses")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/student-courses.schema.json"))
                .body("[0].id", equalTo("sqa"));

        RestAssured.get("/courses/sqa")
                .then()
                .body("students[0].name", equalTo("Max"));

        RestAssured.delete(studentLocation+"/courses/abc")
                .then()
                .statusCode(404);

        RestAssured.delete(studentLocation+"/courses/sqa")
                .then()
                .statusCode(200);

        RestAssured.get(studentLocation+"/courses")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/student-courses.schema.json"))
                .body("[0]", equalTo(null));
    }

}
