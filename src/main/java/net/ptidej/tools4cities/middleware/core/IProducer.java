package net.ptidej.tools4cities.middleware.core;

import java.util.List;

public interface IProducer<E> {
	
	void fetchData() throws Exception;

	void addObserver(final IConsumer<E> aConsumer);
	
	void notifyObservers(final List<E> results);

}
