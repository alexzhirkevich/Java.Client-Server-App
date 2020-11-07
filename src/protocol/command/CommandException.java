package protocol.command;

import message.MessageException;

public class CommandException extends MessageException {

	public CommandException(byte command){
		super("Invalid "+ Command.class.getName() +": " + command);
	}
}
