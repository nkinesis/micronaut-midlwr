package net.ptidej.tools4cities.middleware.producers;

import net.ptidej.tools4cities.middleware.core.AbstractProducer;
import net.ptidej.tools4cities.middleware.core.IConsumer;
import net.ptidej.tools4cities.middleware.core.IProducer;

public class GeometryProducer extends AbstractProducer<String> implements IProducer<String> {
	// TODO: how to work with different data sets?
	private String city = "";
	private JSONProducer jsonProducer = new JSONProducer("./src/test/data/montreal_geometries.json", null);
	
	public GeometryProducer(String city) {
		this.city = city;
	}
	
	@Override
	public void fetchData() throws Exception {
//		String geometryPath;
//		if (city.equalsIgnoreCase("montreal")) {
//			geometryPath = "./src/test/data/montreal_geometries.json";
//		} else {
//			throw new Exception("City \"" + city + "\" is not supported.");
//		}
		
//		this.jsonProducer = new JSONProducer(geometryPath, null);
		this.jsonProducer.fetchData();
	}
	
	@Override 
	public void addObserver(final IConsumer<String> aConsumer) {
		this.jsonProducer.addObserver(aConsumer);
	}

}
