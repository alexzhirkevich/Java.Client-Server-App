package message.connection;

import message.Message;
import protocol.command.Command;
import protocol.command.CommandException;

import java.io.Serializable;

public class MessageDisconnect extends Message implements Serializable {

	private static final long serialVersionUID = 1L;

	public MessageDisconnect() throws CommandException {
		super(Command.END);
	}
}
