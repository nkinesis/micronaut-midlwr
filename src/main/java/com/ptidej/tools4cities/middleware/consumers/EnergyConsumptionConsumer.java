package com.ptidej.tools4cities.middleware.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;
import com.ptidej.tools4cities.middleware.middleware.AbstractConsumer;
import com.ptidej.tools4cities.middleware.middleware.IConsumer;
import com.ptidej.tools4cities.middleware.middleware.IOperation;
import com.ptidej.tools4cities.middleware.middleware.IProducer;

public class EnergyConsumptionConsumer extends AbstractConsumer<JsonObject> implements IConsumer<JsonObject> {

	private ArrayList<JsonObject> results;
	
	public EnergyConsumptionConsumer(Set<IProducer<JsonObject>> setOfProducers, Set<IOperation> setOfOperations) {
		super(setOfProducers, setOfOperations);
	}

	@Override
	public List<JsonObject> getResults() {
		return this.results;
	}
	
	@Override
	public final void newDataAvailable(List<JsonObject> data) {
		try {
			this.results = new ArrayList<JsonObject>();
			this.results.addAll((ArrayList<JsonObject>) data);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
