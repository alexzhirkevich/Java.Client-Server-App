package application.objectstream.message.order;

import application.objectstream.message.Message;
import application.protocol.command.CommandException;

import java.io.Serializable;

public class MessageOrder extends Message implements Serializable {

	public static final long serialVersionUID = 1L;

	String address;
	String order;
	String phone;
	String name;

	public MessageOrder(String address, String order,String phone, String name) throws CommandException {
		super(Message.ORDER);
		this.address = address;
		this.order = order;
		this.phone = phone;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public String getOrder() {
		return order;
	}

}
