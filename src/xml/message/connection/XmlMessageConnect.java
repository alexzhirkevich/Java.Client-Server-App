package xml.message.connection;

import protocol.command.CommandException;
import xml.message.XmlMessage;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class XmlMessageConnect extends XmlMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	public XmlMessageConnect() throws CommandException {
		super(CONNECT);
	}
}
