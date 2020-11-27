package xml.message.menu;

import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.ResultException;
import xml.message.XmlMessageResult;

import java.io.Serializable;
import java.util.Arrays;

public class XmlMessageMenuResult extends XmlMessageResult implements Serializable {

	public static final long serialVersionUID = 1L;

	String[] options;

	public XmlMessageMenuResult(byte result, String[] options) throws ResultException, CommandException {
		super(Command.MENU,result);
		this.options = Arrays.copyOf(options,options.length);
	}

	public String[] getOptions() {
		return Arrays.copyOf(options,options.length);
	}
}
