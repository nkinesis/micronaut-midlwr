package net.ptidej.tools4cities.middleware.core;

import java.util.List;

public interface IConsumer<E> {

	void newDataAvailable(List<E> data);
	
	List<E> getResults();
		
}
