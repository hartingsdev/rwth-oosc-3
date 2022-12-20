package de.rwth.swc.universitymanagement;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.equalTo;

public abstract class AbstractDataTests {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * Create a course with the REST api and check if parameters are correct in created entity.
     */
    Response createCourse(String courseId, String name, String instituteId, int credits) throws JSONException {
        JSONObject requestBody = new JSONObject()
                .put("name", name)
                .put("instituteId", instituteId)
                .put("credits", credits);

        return RestAssured
                .with()
                .contentType("application/json")
                .body(requestBody.toString())
                .put("/courses/"+courseId);
    }

    ValidatableResponse createInstitute(String instituteId, String instituteMail) throws JSONException {
        JSONObject requestBody = new JSONObject()
                .put("id", instituteId)
                .put("mail", instituteMail);

        return RestAssured
                .with()
                .contentType("application/json")
                .body(requestBody.toString())
                .put("/institutes/"+instituteId)
                .then()
                .assertThat().body("id", equalTo(instituteId))
                .assertThat().body("mail", equalTo(instituteMail));
    }

    ValidatableResponse createStudent(String name, String surname) throws JSONException {
        JSONObject requestBody = new JSONObject()
                .put("name", name)
                .put("surname", surname);

        return RestAssured
                .with()
                .contentType("application/json")
                .body(requestBody.toString())
                .post("/students")
                .then()
                .assertThat().body("name", equalTo(name))
                .assertThat().body("surname", equalTo(surname));
    }
}
