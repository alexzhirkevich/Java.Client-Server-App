package xml.message.connection;

import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.Result;
import protocol.result.ResultException;
import xml.message.XmlMessageException;
import xml.message.XmlMessageResult;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class XmlMessageDisconnectResult extends XmlMessageResult implements Serializable {

	private static final long serialVersionUID = 1L;

	public XmlMessageDisconnectResult() throws ResultException, CommandException {
		super(Command.DISCONNECT, Result.INVALID);
	}

	public XmlMessageDisconnectResult(byte result) throws ResultException, CommandException {
		super(Command.DISCONNECT, result);
	}

	public XmlMessageDisconnectResult(byte result, String message) throws ResultException, CommandException {
		super(Command.DISCONNECT,result,message);
	}
}
