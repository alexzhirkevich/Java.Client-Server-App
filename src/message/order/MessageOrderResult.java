package message.order;
import message.MessageException;
import message.MessageResult;
import protocol.command.Command;

import java.io.Serializable;

public class MessageOrderResult extends MessageResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private int number;

	public MessageOrderResult(byte result, int number) throws MessageException {
		super(Command.ORDER, result);
		this.number = number;
	}

	public MessageOrderResult(byte result, String message, int number) throws MessageException{
		super(Command.ORDER,result,message);
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}
