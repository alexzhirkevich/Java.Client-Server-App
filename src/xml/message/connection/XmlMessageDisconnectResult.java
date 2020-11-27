package xml.message.connection;

import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.ResultException;
import xml.message.XmlMessageException;
import xml.message.XmlMessageResult;

import java.io.Serializable;

public class XmlMessageDisconnectResult extends XmlMessageResult implements Serializable {

	private static final long serialVersionUID = 1L;

	public XmlMessageDisconnectResult(byte result) throws ResultException, CommandException {
		super(Command.DISCONNECT, result);
	}

	public XmlMessageDisconnectResult(byte result, String message) throws ResultException, CommandException {
		super(Command.DISCONNECT,result,message);
	}
}
