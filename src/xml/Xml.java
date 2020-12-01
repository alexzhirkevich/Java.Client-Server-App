package xml;


import protocol.Config;
import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.Result;
import protocol.result.ResultException;
import xml.message.XmlMessage;
import xml.message.XmlMessageResult;
import xml.message.connection.*;
import xml.message.context.*;
import xml.message.menu.*;
import xml.message.order.*;

import java.io.*;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class Xml implements Serializable{

	private static final long serialVersionUID = 1L;

	public static String lastQueryError = null;

	public static void toXml(Xml msg , OutputStream os ) throws JAXBException, IOException {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Config.XML_MAX)) {
			JAXBContext context = JAXBContext.newInstance(msg.getClass());
			Marshaller m = context.createMarshaller();
			m.marshal(msg, byteArrayOutputStream);
			byteArrayOutputStream.flush();
			os.write(byteArrayOutputStream.toByteArray());
		}
	}

	public static Xml fromXml( Class<? extends Xml> what, InputStream is ) throws JAXBException, IOException {
		byte[] data = new byte[Config.XML_MAX];
		int len = is.read(data,0,Config.XML_MAX);
		try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Arrays.copyOf(data,len))) {
			JAXBContext context = JAXBContext.newInstance(what);
			Unmarshaller u = context.createUnmarshaller();
			return (Xml)u.unmarshal(byteArrayInputStream);
		}
	}

	public static void writeMsg(DataOutputStream os, Xml msg) throws JAXBException, IOException {
		writeViaByteArray(os, msg);
	}

	public static Xml readMsg(DataInputStream is) throws JAXBException, IOException, ClassNotFoundException {
		return readViaByteArray(is);
	}

	public static void writeViaByteArray(DataOutputStream os, Xml msg)  throws JAXBException, IOException {
		try(ByteArrayOutputStream bufOut = new ByteArrayOutputStream(Config.XML_MAX)) {
			try (DataOutputStream out = new DataOutputStream(bufOut)) {
				String name = msg.getClass().getName();
				out.writeUTF(name);
				Xml.toXml(msg, out);
				out.flush();
			}
			byte[] res = bufOut.toByteArray();
			os.writeInt(res.length);
			os.write(res);
			os.flush();
		}
	}

	@SuppressWarnings("unchecked")
	public static Xml readViaByteArray(DataInputStream is) throws JAXBException, IOException, ClassNotFoundException {
		int length = is.readInt();
		byte[] raw = new byte[length];
		int idx = 0, num = length;
		while ( idx < num ) {
			int n = is.read(raw, idx, num - idx);
			idx += n;
		}
		try (ByteArrayInputStream bufIn = new ByteArrayInputStream(raw)) {
			try (DataInputStream in = new DataInputStream(bufIn)) {
				String name = in.readUTF();
				return Xml.fromXml((Class<? extends Xml>)Class.forName( name ), in);
			}
		}
	}

	public static boolean query2(XmlMessage msg, DataInputStream is, DataOutputStream os)
			throws JAXBException, IOException, CommandException {
		// client
		XmlMessageContext ctx = new XmlMessageContext(msg);
		Xml.toXml(ctx, os);
		os.flush();
		XmlMessageResult res = (XmlMessageResult) Xml.fromXml(XmlMessageContextResult.class, is);
		if (res.checkError()) {
			lastQueryError = null;
			Xml.toXml(msg, os);
			os.flush();
			return true;
		}
		lastQueryError = res.getErrorMessage();
		return false;
	}

	public static Class<? extends XmlMessageResult> getResultClass(byte id) throws ResultException {
		switch (id) {
			case Command.CONTEXT:
				return XmlMessageContextResult.class;
			case Command.CONNECT:
				return XmlMessageConnectResult.class;
			case Command.MENU:
				return XmlMessageMenuResult.class;
			case Command.ORDER:
				return XmlMessageOrderResult.class;
			case Command.DISCONNECT:
				return XmlMessageDisconnectResult.class;
			default:
				throw new ResultException(id);
		}
	}

	public static Class<? extends XmlMessage> getMessageClass(byte id) {

		switch (id) {
			case Command.CONTEXT:
				return XmlMessageContext.class;
			case Command.CONNECT:
				return XmlMessageConnect.class;
			case Command.MENU:
				return XmlMessageMenu.class;
			case Command.ORDER:
				return XmlMessageOrder.class;
			case Command.DISCONNECT:
				return XmlMessageDisconnect.class;
			default:

		}
		return null;
	}

	public static XmlMessageResult query(XmlMessage msg, DataInputStream is, DataOutputStream os, Class<? extends XmlMessageResult> what)
			throws JAXBException, IOException, CommandException {
		// client
		if ( query2( msg, is, os)) {
			XmlMessageResult res = (XmlMessageResult) Xml.fromXml(what, is);
			return res;
		}
		return null;
	}

	public static XmlMessage getXmlMessage(DataInputStream is, DataOutputStream os)
			throws JAXBException, ClassNotFoundException, IOException, ResultException, CommandException {

		XmlMessageContext ctx = (XmlMessageContext) Xml.fromXml(XmlMessageContext.class, is);
		Class<? extends XmlMessage> what = ctx.getXmlClass();
		if ( what != null ) {
			Xml.toXml( new XmlMessageContextResult(Result.OK), os);
		}
		else {
			Xml.toXml( new XmlMessageContextResult(Result.CLASS_ERROR, "Invalid XML-class ID"), os);
		}
		os.flush();
		if ( what != null ) {
			XmlMessage res = (XmlMessage) Xml.fromXml(what, is);
			return res;
		}
		return null;
	}

	public static void answer(XmlMessageResult msg,  DataOutputStream os) throws JAXBException, IOException {
		//server
		Xml.toXml(msg, os);
		os.flush();
	}

}
