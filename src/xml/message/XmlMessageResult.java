package xml.message;

import objectstream.message.Message;
import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.Result;
import protocol.result.ResultException;
import xml.Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class XmlMessageResult extends Xml implements Result, Serializable {

	public static class Data implements Serializable {

		private static final long serialVersionUID = 1L;

		private byte id;
		private byte command;
		private String errMessage;

		public Data() {
			command = Command.INVALID;
			id = Result.INVALID;
		}

		@XmlAttribute
		public byte getID() {
			return id;
		}
		public void setID(byte id) throws ResultException {
			if (!isValid(id))
				throw new ResultException(id);
			this.id = id;
		}

		@XmlAttribute
		public byte getCommand(){
			return command;
		}
		public void setCommand(byte command) throws CommandException {
			if (!Message.isValid(command))
				throw new CommandException(command);
			this.command = command;
		}

		public boolean checkError() {
			return id != Result.OK;
		}

		@XmlAttribute
		public String getErrorMessage() {
			return errMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errMessage = errorMessage;
		}

		public String toString() {
			return "" + id + ", " + ", " + errMessage;
		}

	}

	private static final long serialVersionUID = 1L;

	@XmlElement
	Data data;

	public XmlMessageResult() {
		data = new Data();
	}

	public XmlMessageResult(byte command, byte result) throws ResultException, CommandException {
		this();
		setup(command, result);
	}

	public XmlMessageResult(byte command, byte id, String errorMessage) throws ResultException, CommandException {
		this();
		setup(command, id, errorMessage);
	}

	protected XmlMessageResult.Data getData(){
		return data;
	}

	public byte getID() {
		return getData().getID();
	}

	public boolean checkError() {
		return getData().checkError();
	}

	public String getErrorMessage() {
		return getData().getErrorMessage();
	}

	protected void setup(byte command, byte id, String errorMessage ) throws ResultException, CommandException {
		setup(command, id);
		getData().setErrorMessage(errorMessage);
	}

	protected void setup(byte command, byte id) throws ResultException, CommandException {
		getData().setID(id);
		getData().setCommand(command);
		getData().setErrorMessage("");
	}

	public static boolean isValid(byte result){
		return result >= OK && result <= UNKNOWN_ERROR;
	}

	@Override
	public String toString() {
		return getData().toString();
	}

}
