package xml.message.order;

import protocol.command.CommandException;
import xml.message.XmlMessage;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

public class XmlMessageOrder extends XmlMessage implements Serializable {

	public static final long serialVersionUID = 1L;

	@XmlElement
	String address;

	@XmlElement
	String order;

	public XmlMessageOrder(String address, String order) throws CommandException {
		super(XmlMessage.ORDER);
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
