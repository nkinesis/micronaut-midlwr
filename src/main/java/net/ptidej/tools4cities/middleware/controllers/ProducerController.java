package net.ptidej.tools4cities.middleware.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

@Controller("/apply") 
@Secured(SecurityRule.IS_ANONYMOUS)
public class ProducerController {
	
	@Post("/sync")
    public String sync(@Body String steps) {
		
		JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
		SequentialRunner deckard = new SequentialRunner(stepsObject);
		Thread runnerTask = new Thread() {
		    public void run() {
		    	deckard.runSteps();
				while (!deckard.isDone()) {
					System.out.println("Terribly busy waiting!");
				}
		    }  
		};
		runnerTask.start();
		
		try {
			runnerTask.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		IDataStore store = InMemoryDataStore.getInstance();
		String runnerId = deckard.getMetadata("id").toString();
        return store.get(runnerId).getResultJSONString();
    }

	@Post("/async")
    public String async(@Body String steps) {
		
		JsonObject stepsObject = JsonParser.parseString(steps).getAsJsonObject();
		SequentialRunner aRunner = new SequentialRunner(stepsObject);
		aRunner.runSteps();
		
        return "Hello! The runner " + aRunner.getMetadata("id") + " is currently working on your request. Please make a GET request to /apply/async/ " + aRunner.getMetadata("id") + " to find out your request status.";
    }
	
	@Get("/async/{runnerId}")
    public String asyncId(@PathVariable String runnerId) {
		
		InMemoryDataStore store = InMemoryDataStore.getInstance();
		Object storeResult = store.get(runnerId);
		
		if (storeResult != null) {
			return store.get(runnerId).getResultJSONString();
		}
		return "Sorry, your request result is not ready yet. Please try again later."; 
    }
	
	@Get("/ping")
    public String ping() {
		Date timeObject = Calendar.getInstance().getTime();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(timeObject);
		return "pong - " + timeStamp;
	}
	
	

}

