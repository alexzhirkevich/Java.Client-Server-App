package xml.message.order;
import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.ResultException;
import xml.message.XmlMessageException;
import xml.message.XmlMessageResult;

import java.io.Serializable;

public class XmlMessageOrderResult extends XmlMessageResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private int number;

	public XmlMessageOrderResult(byte result, int number) throws ResultException, CommandException {
		super(Command.ORDER, result);
		this.number = number;
	}

	public XmlMessageOrderResult(byte result, String message, int number) throws ResultException, CommandException {
		super(Command.ORDER,result, message);
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}
