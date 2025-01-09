package net.ptidej.tools4cities.middleware.producers;

import net.ptidej.tools4cities.middleware.core.AbstractProducer;
import net.ptidej.tools4cities.middleware.core.IProducer;
import net.ptidej.tools4cities.middleware.core.IRunner;

public class GeometryProducer extends AbstractProducer<String> implements IProducer<String> {
	private String city;
	private JSONProducer jsonProducer;
	
	public GeometryProducer(String city) {
		this.city = city;
		
		if (this.city.equalsIgnoreCase("montreal")) {
			jsonProducer = new JSONProducer("./src/test/data/montreal_geometries.json", null);
		} else {
			throw new UnsupportedOperationException("City " + this.city + " is not currently supported");
		}
	}
	
	@Override
	public void fetch() {
		this.jsonProducer.fetch();
	}
	
	@Override 
	public void addObserver(final IRunner aRunner) {
		this.jsonProducer.addObserver(aRunner);
	}

}