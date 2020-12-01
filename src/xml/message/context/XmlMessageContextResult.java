package xml.message.context;

import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.Result;
import protocol.result.ResultException;
import xml.message.XmlMessageResult;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>MessageContextResult class
 * @author Sergey Gutnikov
 * @version 1.0
 */
@XmlRootElement
public class XmlMessageContextResult extends XmlMessageResult implements Serializable {

	private static final long serialVersionUID = 1L;

	public XmlMessageContextResult() throws ResultException, CommandException {
		super(Command.CONTEXT, Result.INVALID);
	}

	public XmlMessageContextResult(byte result) throws ResultException, CommandException {
		super.setup(Command.CONTEXT, result);
	}
	
	public XmlMessageContextResult(byte result, String errorMessage ) throws ResultException, CommandException {
		super( Command.CONTEXT, result, errorMessage );
	}
	

}
