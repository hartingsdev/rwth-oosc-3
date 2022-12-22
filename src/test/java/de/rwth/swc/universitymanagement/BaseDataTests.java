package de.rwth.swc.universitymanagement;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpHeaders;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseDataTests extends AbstractDataTests {

    /**
     * This test should fail because there is no institute "swc" defined yet.
     */
    @Test
    void testUnknownInstitute() throws Exception {
        createCourse("abc", "abc course", "nonexistent", 6)
                .then().assertThat().statusCode(400);
    }

    /**
     * Test normal creation of a course with previously creating a institute for that course.
     */
    @ParameterizedTest
    @MethodSource
    void testCourseCreation(String courseId, String courseName, String instituteId, int credits) throws Exception {
        createInstitute(instituteId, "swc@rwth-aachen.de")
                .assertThat().statusCode(201);

        Response crsResponse = createCourse(courseId, courseName, instituteId, credits);
        crsResponse
                .then().assertThat().statusCode(201)
                .header(HttpHeaders.LOCATION, endsWith("/courses/oosc"));
        validateCourseBody(crsResponse, courseId, courseName, instituteId, credits);

        crsResponse = createCourse(courseId, courseName, instituteId, 7);
        crsResponse.then()
                .statusCode(200)
                .body("credits", equalTo(7));
        validateCourseBody(crsResponse, courseId, courseName, instituteId, 7);

        crsResponse = RestAssured.get("/courses/oosc");
        validateCourseBody(crsResponse, courseId, courseName, instituteId, 7);
    }

    private static Stream<Arguments> testCourseCreation() {
        return Stream.of(
                Arguments.of("oosc", "Object Oriented Software Construction", "swc", 6)
        );
    }

    /**
     * Test institute creation via api.
     */
    @Test
    void testInstituteCreation() throws JSONException {
        createInstitute("ide", "asdwq")
                .assertThat().statusCode(201)
                .header(HttpHeaders.LOCATION, endsWith("/institutes/ide"));
        createInstitute("ide", "ide@rwth-aachen.de")
                .assertThat().statusCode(200);

        RestAssured.get("institutes/ide")
                .then()
                .assertThat().body(matchesJsonSchemaInClasspath("schemas/institute.schema.json"));
    }

    @Test
    void testStudentCreation() throws JSONException {
        String location = createStudent("Max", "Mustermann")
                .assertThat().statusCode(201)
                .extract().response().getHeader(HttpHeaders.LOCATION);

        RestAssured.get(location)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/student.schema.json"))
                .body("name", equalTo("Max"))
                .body("surname", equalTo("Mustermann"));
    }

    private ValidatableResponse validateCourseBody(Response resp, String courseId, String name, String instituteId, int credits) {
        return resp.then().assertThat()
                .body("id", equalTo(courseId))
                .body("name", equalTo(name))
                .body("institute.id", equalTo(instituteId))
                .body("credits", equalTo(credits))
                .body(matchesJsonSchemaInClasspath("schemas/course.schema.json"));
    }
}
