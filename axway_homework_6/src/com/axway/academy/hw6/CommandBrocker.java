package com.axway.academy.hw6;

import java.util.ArrayList;

/**
 * This class is used to handle commands - register them and execute them
 * @author Mila I
 *
 */

public class CommandBrocker {
	
	/**
	 * A list of all the commands passed to the broker.
	 */
	private ArrayList<Command> commands = new ArrayList<Command>();
	
	/*
	 * Static variable is used to assure that only one instance would be used each time
	 */
	private static CommandBrocker cb = null;
	
	/**
	 * This constructor exists only to ensure that objects would not be instantiated
	 */
	private CommandBrocker() {}
	
	/**
	 * Used to return an instance of the object.
	 * @return the one and only single instance of the object
	 */
	public static CommandBrocker getInstance() {
		if (cb == null) {
			cb = new  CommandBrocker();
		}
		return cb;
	}
	
	/*
	 * Adds a command to the list.
	 * @param c - the command that is to be added to the list
	 */
	public void registerCommand(Command c) {
		commands.add(c);
	}
	
	/**
	 * Executes all commands in the list
	 */
	public void execute() {
		for (Command command : commands) {
			command.execute();
		}
	}

}
