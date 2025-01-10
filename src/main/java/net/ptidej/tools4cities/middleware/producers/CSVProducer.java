package net.ptidej.tools4cities.middleware.producers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ptidej.tools4cities.middleware.core.AbstractProducer;
import net.ptidej.tools4cities.middleware.core.IProducer;
import net.ptidej.tools4cities.middleware.core.RequestOptions;

/**
 * This producer can load CSV from a file or remotely via an HTTP request.
 */
public class CSVProducer extends AbstractProducer<String> implements IProducer<String> {

	public CSVProducer(String filePath, RequestOptions fileOptions) {
		this.filePath = filePath;
		this.fileOptions = fileOptions;
	}

	// I added the error handling to ensure I actually read my local file

	@Override
	public void fetch() {
		final String csvString = new String(this.fetchFromPath());

		// split CSV string by line, add lines to the list
		final List<String> csvLines = new ArrayList<String>();
		csvLines.addAll(Arrays.asList(csvString.split(System.lineSeparator())));

		this.notifyObservers();

	}

}