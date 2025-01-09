package net.ptidej.tools4cities.middleware.runners;

import net.ptidej.tools4cities.middleware.core.IDataStore;
import net.ptidej.tools4cities.middleware.core.IOperation;
import net.ptidej.tools4cities.middleware.core.IProducer;
import net.ptidej.tools4cities.middleware.core.IRunner;
import net.ptidej.tools4cities.middleware.core.MiddlewareEntity;
import net.ptidej.tools4cities.middleware.datastores.InMemoryDataStore;

/**
 * 
 * This Runner runs a single Producer with no Operations. For test only.
 * 
 */
public class LazyRunner extends MiddlewareEntity implements IRunner {

	private boolean isDone = false;
	private IProducer<?> producer;
	
	public LazyRunner(IProducer<?> producer) {
		this.producer = producer;
	}
	
	public boolean isDone() {
		return this.isDone;
	}
	
	private void setAsDone() {
		this.isDone = true;
	}
	

	@Override
	public void runSteps() {
		if (this.producer != null) {
			this.producer.addObserver(this);
			this.producer.fetch();
		}
	}

	@Override
	public void applyNextOperation(IProducer<?> producer) {
		System.out.println("This runner does not support operations!");
	}

	@Override
	public void newOperationApplied(IOperation<?> operation) {
		System.out.println("This runner does not support operations!");
	}

	@Override
	public void newDataAvailable(IProducer<?> producer) {
		this.storeResults(producer);
		this.setAsDone();
		System.out.println("Run completed!");
	}

	@Override
	public void storeResults(IProducer<?> producer) {
		IDataStore store = InMemoryDataStore.getInstance();
		String runnerId = this.getMetadata("id").toString();
		store.set(runnerId, producer);
	}

}
