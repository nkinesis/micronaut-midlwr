package net.ptidej;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;

@MicronautTest 
public class EnergyConsumptionTest {

    @Test
    public void testEndpoint(RequestSpecification spec) { 
    	final String queryParams = "startDatetime=2021-01-01T13:00:00.000Z&endDatetime=2021-01-02T00:45:00.000Z&postalCode=H2Y2E4";
        spec    
            .when()
                .get("/consumers/energyConsumption?" + queryParams)
            .then()
                .statusCode(200)
                .body(containsString("timestamp"));
    }
}