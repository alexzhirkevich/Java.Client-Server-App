package xml.message;

import protocol.command.Command;
import protocol.command.CommandException;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

public class XmlMessage extends Xml implements Command, Serializable {

	public static class Data implements Serializable {

		private static final long serialVersionUID = 1L;

		protected byte id;

		public Data() {
			id = Command.INVALID;
		}

		@XmlAttribute
		public byte getID() {
			return id;
		}
		public void setID(byte id) throws CommandException {
			if (!isValid(id))
				throw new CommandException(id);
			this.id = id;
		}

		public String toString() {
			return "" + id;
		}
	}

	Data data = new Data();

	protected XmlMessage.Data getData(){
		return data;
	}

	private static final long serialVersionUID = 1L;

	protected XmlMessage() {}

	protected XmlMessage(byte id ) throws CommandException {
		setup(id);
	}

	public static boolean isValid(byte command) {
		return command >= CONNECT && command <= DISCONNECT;
	}

	public byte getId() {
		return getData().getID();
	}

	protected void setup(byte id ) throws CommandException {
		getData().setID(id);
	}

}
