package xml.message.menu;

import protocol.command.Command;
import protocol.command.CommandException;
import xml.message.XmlMessage;

import java.io.Serializable;

public class XmlMessageMenu extends XmlMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	public XmlMessageMenu() throws CommandException {
		super(Command.MENU);
	}
}
