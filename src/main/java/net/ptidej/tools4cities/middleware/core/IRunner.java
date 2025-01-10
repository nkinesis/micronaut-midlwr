package net.ptidej.tools4cities.middleware.core;

/**
*
* The Runner entity is responsible for:
* - Resolving Producer/Operation names into classes
* - Instantiating classes while passing the necessary parameters
* - Telling Producers to fetch data and performing an operation 
* - If there is more than one operation to be performed, keep executing operations in order, where O2 will be applied in the result of O1
* - Storing outputs on the DataStore so they can be retrieved by the requester, either immediately (synchronously) or asynchronously
* 
*/
public interface IRunner  {
	
	// execution
	void runSteps();
	void applyNextOperation(IProducer<?> producer);
	void storeResults(IProducer<?> producer);
	
	// execution state control
	boolean isDone();
    void setAsDone();
	
	// receiving notifications from other entities
	void newOperationApplied(IOperation<?> operation);
    void newDataAvailable(IProducer<?> producer);

}
