package message;

import protocol.command.Command;
import protocol.command.CommandException;

import java.io.Serializable;

public class Message implements Command, Serializable {

	private static final long serialVersionUID = 1L;

	private final byte id;

	public Message(byte command) throws CommandException {
		if (!isValid(command)) {
			throw new CommandException(command);
		}
		this.id = command;
	}

	public static boolean isValid(byte command) {
		return command >= START && command <= END;
	}

	public byte getId() {
		return id;
	}
}
