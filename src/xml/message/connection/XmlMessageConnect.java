package xml.message.connection;

import protocol.command.Command;
import protocol.command.CommandException;
import xml.message.XmlMessage;

import java.io.Serializable;

public class XmlMessageConnect extends XmlMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	public XmlMessageConnect() throws CommandException {
		super.setup(Command.CONNECT);
	}
}
