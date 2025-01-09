package net.ptidej.tools4cities.middleware.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import net.ptidej.tools4cities.middleware.core.IDataStore;
import net.ptidej.tools4cities.middleware.datastores.InMemoryDataStore;
import net.ptidej.tools4cities.middleware.producers.GeometryProducer;
import net.ptidej.tools4cities.middleware.runners.LazyRunner;

@Controller("/exp") 
@Secured(SecurityRule.IS_ANONYMOUS)
public class ExperimentalController {
	
	@Get("/abc")
    public String abc() {
		
		GeometryProducer producer = new GeometryProducer("montreal");
		LazyRunner bob = new LazyRunner(producer);
		Thread runnerTask = new Thread() {
		    public void run() {
		    	bob.runSteps();
				while (!bob.isDone()) {
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
		String runnerId = bob.getMetadata("id").toString();
        return store.get(runnerId).getResultJSONString();
    }
	

}

