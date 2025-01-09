package net.ptidej.tools4cities.middleware.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import net.ptidej.tools4cities.middleware.core.IDataStore;
import net.ptidej.tools4cities.middleware.datastores.InMemoryDataStore;
import net.ptidej.tools4cities.middleware.runners.SequentialRunner;

@Controller("/strings") 
@Secured(SecurityRule.IS_ANONYMOUS)
public class StringController {

	@Get("/async")
    public String async(@Body String steps) {
		
		JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
		SequentialRunner aRunner = new SequentialRunner(stepsObject);
		aRunner.runSteps();
		
        return "Hello! We are working in your request number " + aRunner.getMetadata("id") + ". Please use /async/{id} to find out your request status.";
    }
	
	@Get("/async/{runnerId}")
    public String asyncId(@PathVariable String runnerId) {
		
		InMemoryDataStore store = InMemoryDataStore.getInstance();
		Object storeResult = store.get(runnerId);
		
		if (storeResult != null) {
			return store.get(runnerId).getResultJSONString();
		}
		return "Sorry, your data is not ready yet. Please try again later."; 
    }
	
	@Post("/sync")
    public String sync(@Body String steps) {
		
		JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
		SequentialRunner deckard = new SequentialRunner(stepsObject);
		Thread stepExecution = new Thread() {
		    public void run() {
		    	deckard.runSteps();
				while (!deckard.isDone()) {
					System.out.println("Terribly busy waiting!");
				}
		    }  
		};
		stepExecution.start();
		
		try {
			stepExecution.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		IDataStore store = InMemoryDataStore.getInstance();
		String runnerId = deckard.getMetadata("id").toString();
        return store.get(runnerId).getResultJSONString();
    }

}

