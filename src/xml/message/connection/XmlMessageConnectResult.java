package xml.message.connection;

import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.ResultException;
import xml.message.XmlMessageResult;

import java.io.Serializable;

public class XmlMessageConnectResult extends XmlMessageResult implements Serializable {

	private static final long serialVersionUID = 1L;

	public XmlMessageConnectResult(byte result) throws ResultException, CommandException {
		super(Command.CONNECT, result);
	}

	public XmlMessageConnectResult(byte result, String message) throws ResultException, CommandException {
		super(Command.CONNECT,result,message);
	}
}
