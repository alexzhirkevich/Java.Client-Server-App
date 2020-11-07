package message.menu;

import message.Message;
import message.MessageException;
import message.MessageResult;
import protocol.command.Command;

import java.io.Serializable;

public class MessageMenu extends Message implements Serializable {

	private static final long serialVersionUID = 1L;

	public MessageMenu() throws MessageException {
		super(Command.MENU);
	}
}
