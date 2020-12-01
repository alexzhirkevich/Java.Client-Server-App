package xml.message.order;

import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.Result;
import xml.message.XmlMessage;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class XmlMessageOrder extends XmlMessage implements Serializable {

	public static final long serialVersionUID = 1L;

	@XmlElement
	String address;

	@XmlElement
	String order;

	public XmlMessageOrder() throws CommandException {
		super(ORDER);
		address = "unknown";
		order = "unknown";
	}

	public XmlMessageOrder(String address, String order) throws CommandException {
		super(Command.ORDER);
		this.address = address;
		this.order = order;
	}

	public String getAddress() {
		return address;
	}

	public String getOrder() {
		return order;
	}

}
