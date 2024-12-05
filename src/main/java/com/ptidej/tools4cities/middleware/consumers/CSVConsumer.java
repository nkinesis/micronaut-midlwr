package com.ptidej.tools4cities.middleware.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ptidej.tools4cities.middleware.middleware.AbstractConsumer;
import com.ptidej.tools4cities.middleware.middleware.IConsumer;
import com.ptidej.tools4cities.middleware.middleware.IOperation;
import com.ptidej.tools4cities.middleware.middleware.IProducer;

public class CSVConsumer extends AbstractConsumer<String> implements IConsumer<String> {

	private List<String> results;

	public CSVConsumer(final Set<IProducer<String>> setOfProducers, final Set<IOperation> setOfOperations) {
		super(setOfProducers, setOfOperations);
	}

	@Override
	public List<String> getResults() {
		return results;
	}

	@Override
	public final void newDataAvailable(List<String> data) {
		try {
			this.results = new ArrayList<String>();
			this.results.addAll((ArrayList<String>) data);
			for (IOperation operation : this.setOfOperations) { 
				this.results = operation.perform(this.results);	
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}