package message.connection;

import message.Message;
import protocol.command.Command;
import protocol.command.CommandException;

public class MessageConnect extends Message {

	public MessageConnect() throws CommandException {
		super(Command.START);
	}
}
