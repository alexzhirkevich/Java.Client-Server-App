package xml.message.order;

import protocol.command.CommandException;
import xml.message.XmlMessage;

import java.io.Serializable;

public class XmlMessageOrder extends XmlMessage implements Serializable {

	public static final long serialVersionUID = 1L;

	String address;
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
