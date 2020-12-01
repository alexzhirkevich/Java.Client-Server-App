package xml.message.context;

import protocol.command.Command;
import protocol.command.CommandException;
import xml.Xml;
import xml.message.XmlMessage;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class XmlMessageContext extends XmlMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement
	public Byte classID;

	public XmlMessageContext() throws CommandException {
		super(CONTEXT);
		classID = 0;
	}

	public XmlMessageContext(XmlMessage msg ) throws CommandException {
		super(CONTEXT);
		classID = msg.getId();
	}

	public Class<? extends XmlMessage> getXmlClass()
			throws ClassNotFoundException {
		return Xml.getMessageClass(classID);
	}
}
