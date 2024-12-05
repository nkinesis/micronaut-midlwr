package com.ptidej.tools4cities.middleware.producers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ptidej.tools4cities.middleware.middleware.AbstractProducer;
import com.ptidej.tools4cities.middleware.middleware.IProducer;

public class EnergyConsumptionProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject> {

	final List<String> energyConsumptionResults;
	final List<String> geometryResults;
	
	public EnergyConsumptionProducer(List<String> energyConsumptionResults, List<String> geometryResults) {
		this.energyConsumptionResults = energyConsumptionResults;
		this.geometryResults = geometryResults;
	}
	
	@Override
	public void fetchData() throws Exception {
		List<JsonObject> result = new ArrayList<JsonObject>();
		for (String ecResult : energyConsumptionResults) {
			JsonObject resultObj = new JsonObject();
			String[] splitEc = ecResult.split(",");
			resultObj.addProperty("timestamp", splitEc[1]);
			resultObj.addProperty("consumption", splitEc[2].replaceAll("\r", ""));
			
			for (String geomResult : geometryResults) {
				if (geomResult.contains(splitEc[0])) {
					final JsonObject geomObj = JsonParser.parseString(geomResult).getAsJsonObject();
					resultObj.addProperty("geometry", geomObj.get("geometry").toString());
					break;
				}
			}
			result.add(resultObj);		
		}
		this.notifyObservers(result);
		
	}

}
