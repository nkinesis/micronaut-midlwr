package com.ptidej.tools4cities.middleware.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ptidej.tools4cities.middleware.consumers.CSVConsumer;
import com.ptidej.tools4cities.middleware.consumers.EnergyConsumptionConsumer;
import com.ptidej.tools4cities.middleware.consumers.JSONConsumer;
import com.ptidej.tools4cities.middleware.middleware.AbstractConsumer;
import com.ptidej.tools4cities.middleware.middleware.IOperation;
import com.ptidej.tools4cities.middleware.middleware.IProducer;
import com.ptidej.tools4cities.middleware.operations.FilterOperation;
import com.ptidej.tools4cities.middleware.operations.FilterRangeOperation;
import com.ptidej.tools4cities.middleware.producers.CSVProducer;
import com.ptidej.tools4cities.middleware.producers.EnergyConsumptionProducer;
import com.ptidej.tools4cities.middleware.producers.JSONProducer;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

@Controller("/consumers/energyConsumption") 
public class EnergyConsumptionController {
	
    @Get("/")
    public String index(
    		@QueryValue(value = "startDatetime") String startDatetime, 
    		@QueryValue(value = "endDatetime") String endDatetime, 
    		@QueryValue(value = "postalCode") String postalCode)  {
    	
    	// create prods
    	final Set<IProducer<String>> csvProducers = new HashSet<>();
    	final Set<IProducer<String>> jsonProducers = new HashSet<>();
    	
    	
    	// start up prods and cons
    	final AbstractConsumer[] results = new AbstractConsumer[2];
    	
    	Thread thread1 = new Thread(() -> {
        	final Set<IOperation> operations = new HashSet<>();
        	operations.add(new FilterRangeOperation(startDatetime, endDatetime));
        	
		    IProducer<String> csvConsumptionProducer = new CSVProducer("./src/test/data/montreal_energy_consumption.csv", null);
		    csvProducers.add(csvConsumptionProducer);
		    results[0] = new CSVConsumer(csvProducers, operations);
    	});
    	thread1.start();
	    
    	Thread thread2 = new Thread(() -> {
    		final Set<IOperation> operations = new HashSet<>();
        	operations.add(new FilterOperation(postalCode, false));
    		
		    IProducer<String> jsonGeometryProducer = new JSONProducer("./src/test/data/montreal_geometries.json", null);
		    jsonProducers.add(jsonGeometryProducer);
		    results[1] = new JSONConsumer(jsonProducers, operations);
    	});
    	thread2.start();
    	

    	try {
			thread1.join();
			thread2.join();
			List<String> consumptionResults = results[0].getResults();
			List<String> geometryResults = results[1].getResults();
			
			EnergyConsumptionProducer ecp = new EnergyConsumptionProducer(consumptionResults, geometryResults);
			Set<IProducer<JsonObject>> ecpProducers = new HashSet<>();
			ecpProducers.add(ecp);
			
			EnergyConsumptionConsumer ecc = new EnergyConsumptionConsumer(ecpProducers, null);
			List<JsonObject> eccResults =  ecc.getResults();
			
			JsonArray mergedResult = new JsonArray();
			for (JsonObject eccResult : eccResults) {
				mergedResult.add(eccResult);
			}
			
			return mergedResult.toString();
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	return "";
    }
}




