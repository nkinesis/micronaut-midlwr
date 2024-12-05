package com.ptidej;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;

@MicronautTest 
public class HelloTest {

    @Test
    public void testHelloEndpoint(RequestSpecification spec) { 
        spec    
            .when()
                .get("/helloWorld")
            .then()
                .statusCode(200)
                .body(containsString("Hello requester :)"));
    }
}