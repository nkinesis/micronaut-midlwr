/**
 * 
 */
package net.ptidej.tools4cities.middleware.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
*
* This implements features common to all Producers, such as reading data from files and URLs and notifying runners
* 
*/
public abstract class AbstractProducer<E> extends MiddlewareEntity implements IProducer<E> {

	protected String filePath;
	//protected RequestOptions fileOptions;
	private IOperation<E> operation;
	private Set<IRunner> runners = new HashSet<>();
	protected ArrayList<E> result;
	
	public AbstractProducer() {
		this.setMetadata("role", "producer");
	}
	
	@Override
	public void setOperation(IOperation operation) {
		this.operation = operation;
	}
	
	@Override
	public void addObserver(final IRunner aRunner) {
		this.runners.add(aRunner);
	}
	
	@Override
	public void fetch() {
		System.out.println("Unimplemented method! This method must be implemented by a subclass.");
	}

	@Override
	public void notifyObservers() {
		try {
			for (final Iterator<IRunner> iterator = this.runners.iterator(); iterator.hasNext();) {

				final IRunner runner = iterator.next();
				runner.newDataAvailable(this);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void applyOperation() {
		this.result = operation.apply(this.result);
		this.notifyObservers();
	}
	
	@Override
	public String getResultJSONString() {
		return this.result.toString();
	}



}
