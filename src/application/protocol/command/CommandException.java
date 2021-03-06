package application.protocol.command;

import application.objectstream.message.MessageException;

public class CommandException extends MessageException {

	public CommandException(byte command) {
		super("Invalid " + Command.class.getName() + ": " + command);
	}
}
