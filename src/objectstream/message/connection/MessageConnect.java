package objectstream.message.connection;

import objectstream.message.Message;
import protocol.command.Command;
import protocol.command.CommandException;

import java.io.Serializable;

public class MessageConnect extends Message implements Serializable {

	private static final long serialVersionUID = 1L;

	public MessageConnect() throws CommandException {
		super(Command.CONNECT);
	}
}
