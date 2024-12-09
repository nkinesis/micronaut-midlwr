package net.ptidej.tools4cities.middleware.controllers;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonArray;
import net.ptidej.tools4cities.middleware.consumers.BuildingConsumer;
import net.ptidej.tools4cities.middleware.core.IConsumer;
import net.ptidej.tools4cities.middleware.core.IOperation;
import net.ptidej.tools4cities.middleware.core.IProducer;
import net.ptidej.tools4cities.middleware.operations.FilterOperation;
import net.ptidej.tools4cities.middleware.operations.FilterRangeOperation;
import net.ptidej.tools4cities.middleware.producers.EnergyConsumptionProducer;
import net.ptidej.tools4cities.middleware.producers.GeometryProducer;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/consumers/energyConsumption") 
@Secured(SecurityRule.IS_ANONYMOUS)
public class EnergyConsumptionController {
	
	private Set<IProducer<String>> getProducers() {
		final Set<IProducer<String>> producers = new HashSet<>();
		IProducer<String> energyConsumptionProducer = new EnergyConsumptionProducer("montreal");
		IProducer<String> geometryProducer = new GeometryProducer("montreal");
		producers.add(energyConsumptionProducer);
		producers.add(geometryProducer);
		return producers;
	}
	
	private Set<IOperation> getOperations(String startDatetime, String endDatetime, String postalCode) {
		final Set<IOperation> operations = new HashSet<>();
		operations.add(new FilterRangeOperation(startDatetime, endDatetime));
		operations.add(new FilterOperation(postalCode, false));
		return operations;
	}
	
    @Get("/")
    public String index(
    		@QueryValue(value = "startDatetime") String startDatetime, 
    		@QueryValue(value = "endDatetime") String endDatetime, 
    		@QueryValue(value = "postalCode") String postalCode)  {
    	
    	JsonArray resultArray = new JsonArray();
    	try {
    		
    		Set<IProducer<String>> producers = getProducers();
    		Set<IOperation> operations = getOperations(startDatetime, endDatetime, postalCode);
    		IConsumer<String> buildingConsumer = new BuildingConsumer(producers, operations);
    		return buildingConsumer.getResults().get(0).toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
    	
		return resultArray.toString();
    }
}




