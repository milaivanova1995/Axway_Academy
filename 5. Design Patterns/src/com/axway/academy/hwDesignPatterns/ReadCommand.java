package com.axway.academy.hwDesignPatterns;

/**
 * This class represents the read command
 * @author Mila I
 *
 */
public class ReadCommand implements Command {
	
	/**
	 * FileOperations object. The FileOperations object is passed to the command 
	 * and the command executes whatever method is needed from the object.
	 */
	private FileOperations fo;
	
	/**
	 * Constructor
	 * @param fo - FileOperatins object
	 * @param fileName - a String taken as a user input
	 */
	public ReadCommand(FileOperations fo) {
		this.fo = fo;
	}

	/**
	 * Executes the command.
	 */
	@Override
	public void execute() {
		fo.readFiles();
	}
	
}
