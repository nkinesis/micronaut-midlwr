package com.ptidej.tools4citites.middleware.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ptidej.tools4cities.middleware.consumers.CSVFilterConsumer;
import com.ptidej.tools4cities.middleware.middleware.IProducer;
import com.ptidej.tools4cities.middleware.producers.CSVProducer;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;


@Controller("/consumers/CSVFilterConsumer") 
public class CSVFilterConsumerController {
	
    @Get("/")
    public String index() {
    	// produce energy consumption data
	    final IProducer<String> producer = new CSVProducer("./src/test/data/example.csv", null);
		final Set<IProducer<String>> producers = new HashSet<IProducer<String>>();
		producers.add(producer);

		// consume energy consumption records and filter them
		final CSVFilterConsumer consumer = new CSVFilterConsumer(producers);
		List<String> filteredCSV = consumer.getResults();

        return "Number of records: " + filteredCSV.size();
    }
    
}