package com.axway.academy.hwDesignPatterns;

/**
 * This class represents the write command
 * @author Mila I
 *
 */
public class WriteCommand implements Command {

	/**
	 * FileOperations object. The FileOperations object is passed to the command 
	 * and the command executes whatever method is needed from the object.
	 */
	private FileOperations fo;
	String newFileName;
	
	/**
	 * Constructor
	 * @param fo - FileOperatins object
	 * @param fileName - a String taken as a user input for the file name
	 */
	public WriteCommand(FileOperations fo) {
		this.fo = fo;
	}

	/**
	 * Executes the command.
	 */
	@Override
	public void execute() {
		fo.writeFile();
	}
}
