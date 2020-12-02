package application.xml.message.connection;

import application.protocol.command.Command;
import application.protocol.command.CommandException;
import application.protocol.result.Result;
import application.protocol.result.ResultException;
import application.xml.message.XmlMessageResult;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class XmlMessageConnectResult extends XmlMessageResult implements Result, Serializable {

	private static final long serialVersionUID = 1L;

	public XmlMessageConnectResult() throws  CommandException {
		setCommand(Command.CONNECT);
	}

	public XmlMessageConnectResult(byte result) throws ResultException, CommandException {
		super(Command.CONNECT, result);
	}

	public XmlMessageConnectResult(byte result, String message) throws ResultException, CommandException {
		super(Command.CONNECT,result,message);
	}
}
