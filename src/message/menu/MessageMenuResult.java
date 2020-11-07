package message.menu;

import message.MessageException;
import message.MessageResult;
import protocol.command.Command;
import protocol.result.Result;

import java.io.Serializable;
import java.util.Arrays;

public class MessageMenuResult extends MessageResult implements Serializable {

	public static final long serialVersionUID = 1L;

	String[] options;

	public MessageMenuResult(byte result, String[] options) throws MessageException {
		super(Command.MENU,result);
		this.options = Arrays.copyOf(options,options.length);
	}

	public String[] getOptions() {
		return Arrays.copyOf(options,options.length);
	}
}
