package com.axway.academy.hw6;

public class ReadCommand implements Command {
	
	/**
	 * FileOperations object. The FileOperations object is passed to the command 
	 * and the command executes whatever method is needed from the object.
	 */
	private FileOperations fo;
	String fileName;
	
	/**
	 * Constructor
	 * @param fo - FileOperatins object
	 * @param fileName - a String taken as a user input
	 */
	public ReadCommand(FileOperations fo, String fileName) {
		this.fo = fo;
		this.fileName = fileName;
	}

	/**
	 * Executes the command.
	 */
	@Override
	public void execute() {
		fo.readFiles(fileName);
	}
	
}
