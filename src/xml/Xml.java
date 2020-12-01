package xml;


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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public abstract class Xml implements Serializable{

	private static final long serialVersionUID = 1L;

	public static String lastQueryError = null;

	public static void toXml( Xml msg, OutputStream os ) throws JAXBException, IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {
			JAXBContext context = JAXBContext.newInstance(msg.getClass());
			Marshaller m = context.createMarshaller();
			m.marshal(msg, bos);
			bos.flush();
			os.write(bos.toByteArray());
		}
	}

	public static Xml fromXml( Class<? extends Xml> what, InputStream is ) throws JAXBException, IOException {
		byte[] data = new byte[1024];
		is.read(data,0,1024);
		try(ByteArrayInputStream bos = new ByteArrayInputStream(data)) {
			JAXBContext context = JAXBContext.newInstance(what);
			Unmarshaller u = context.createUnmarshaller();
			return (Xml)u.unmarshal(bos);
		}
	}

	public static void writeMsg(DataOutputStream os, Xml msg) throws JAXBException, IOException {
		writeViaByteArray(os, msg);
	}

	public static Xml readMsg(DataInputStream is) throws JAXBException, IOException, ClassNotFoundException {
		return readViaByteArray(is);
	}

	public static void writeViaByteArray(DataOutputStream os, Xml msg)  throws JAXBException, IOException {
		try(ByteArrayOutputStream bufOut = new ByteArrayOutputStream(512)) {
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
