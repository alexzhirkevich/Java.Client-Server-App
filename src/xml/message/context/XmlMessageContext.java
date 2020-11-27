package xml.message.context;

import protocol.command.Command;
import protocol.command.CommandException;
import xml.message.Xml;
import xml.message.XmlMessage;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class XmlMessageContext extends XmlMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	public Byte classID;

	public XmlMessage.Data data = new XmlMessage.Data();
	protected XmlMessage.Data getData() {
		return data;
	}

	public XmlMessageContext() throws CommandException {
		super(Command.CONTEXT );
		classID = 0;
	}

	public XmlMessageContext(XmlMessage msg ) throws CommandException {
		super(Command.CONTEXT);
		classID = msg.getId();
	}

	public Class<? extends XmlMessage> getXmlClass()
			throws ClassNotFoundException {
		return Xml.classById(classID);
	}
}
