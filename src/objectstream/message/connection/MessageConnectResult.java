package objectstream.message.connection;

import objectstream.message.MessageException;
import objectstream.message.MessageResult;
import protocol.command.Command;

import java.io.Serializable;

public class MessageConnectResult extends MessageResult implements Serializable {

	private static final long serialVersionUID = 1L;

	public MessageConnectResult(byte result) throws MessageException {
		super(Command.CONNECT, result);
	}

	public MessageConnectResult(byte result, String message) throws MessageException{
		super(Command.CONNECT,result,message);
	}
}
