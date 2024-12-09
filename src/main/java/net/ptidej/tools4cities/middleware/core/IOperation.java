package net.ptidej.tools4cities.middleware.core;

import java.util.List;

public interface IOperation {

	<E> List<E> perform(List<E> inputs) throws Exception;

}
