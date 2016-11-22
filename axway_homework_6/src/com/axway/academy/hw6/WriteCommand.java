package com.axway.academy.hw6;

public class WriteCommand implements Command {

	/**
	 * FileOperations object. The FileOperations object is passed to the command 
	 * and the command executes whatever method is needed from the object.
	 */
	private FileOperations fo;
	String fileName;
	String newFileName;
	StringBuilder text;
	
	/**
	 * Constructor
	 * @param fo - FileOperatins object
	 * @param fileName - a String taken as a user input for the file name
	 */
	public WriteCommand(FileOperations fo, String fn) {
		this.fo = fo;
		this.fileName = fn;
		this.text = fo.readFiles(fn);
		this.newFileName = fo.changeFileName(fn);
	}

	/**
	 * Executes the command.
	 */
	@Override
	public void execute() {
		fo.writeFile(text, newFileName, fileName);
	}
}
