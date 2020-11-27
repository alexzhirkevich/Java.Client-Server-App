package xml.message.connection;

import protocol.command.Command;
import protocol.command.CommandException;
import xml.message.XmlMessage;

import java.io.Serializable;

public class XmlMessageDisconnect extends XmlMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	public XmlMessageDisconnect() throws CommandException {
		super(Command.DISCONNECT);
	}
}
