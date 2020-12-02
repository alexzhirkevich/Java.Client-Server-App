package application.xml.client;

import application.objectstream.client.Client;
import application.objectstream.message.Message;
import application.objectstream.message.MessageException;
import application.protocol.result.Result;
import application.xml.Xml;
import application.xml.message.XmlMessage;
import application.xml.message.XmlMessageResult;
import application.xml.message.connection.XmlMessageConnect;
import application.xml.message.connection.XmlMessageDisconnect;
import application.xml.message.menu.XmlMessageMenu;
import application.xml.message.menu.XmlMessageMenuResult;
import application.xml.message.order.XmlMessageOrder;
import application.xml.message.order.XmlMessageOrderResult;
import application.xml.schema.validator.InvalidSchemaException;

import javax.xml.bind.JAXBException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

public class XmlClient extends Client {

	DataInputStream dataInputStream;
	DataOutputStream dataOutputStream;

	public XmlClient(String host, int port) throws IOException, MessageException, ClassNotFoundException {
		super(host,port);
	}


	protected XmlMessageResult sendMessage(XmlMessage msg) throws IOException, JAXBException, MessageException, InvalidSchemaException {
		if (msg == null)
			return null;

		dataOutputStream.write(msg.getId());
		dataOutputStream.writeUTF(Xml.toXml(msg));

		return (XmlMessageResult)Xml.fromXml(Xml.getResultClass(msg.getClass()),dataInputStream.readUTF());
	}

	@Override
	protected void readMenu() throws MessageException, IOException, ClassNotFoundException {
		XmlMessageMenuResult result = null;
		try {
			result = (XmlMessageMenuResult)sendMessage(new XmlMessageMenu());
		} catch (JAXBException | InvalidSchemaException e) {
			System.err.println(e.getMessage());
		}
		if (result != null && result.checkError()) {
			System.out.println(sMenu);
			for (String line : result.getOptions()) {
				System.out.println(line);
			}
		}
		else {
			System.out.println(sResponseError);
		}
	}

	@Override
	protected void makeOrder() throws MessageException, IOException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.println(sInputAddress);
		String address = sc.nextLine();
		System.out.println(sInputOrder);
		String choice = sc.nextLine();
		XmlMessageOrderResult result = null;
		try {
			result = (XmlMessageOrderResult)sendMessage(new XmlMessageOrder(address, choice));
		} catch (JAXBException | InvalidSchemaException e) {
			System.err.println(e.getMessage());
		}
		if (result != null && result.checkError())
			System.out.println(sOrderNumber + result.getNumber());
		else
			System.out.println(sResponseError);
	}

	@Override
	protected void disconnect() throws IOException, MessageException {
		try {
			if (sendMessage(new XmlMessageDisconnect()).getID() == Result.OK)
				System.out.println(sHaveANiceDay);
			else
				System.out.println(sLostConnection);
		} catch (JAXBException | InvalidSchemaException e) {
			System.err.println(e.getMessage());
		}
		isRunning = false;
		dataInputStream.close();
		dataOutputStream.close();
		clientSocket.close();
	}


	@Override
	public void run() {
		try {
			dataInputStream = new DataInputStream(clientSocket.getInputStream());
			dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			System.err.println(sConnectionFailed);
		}

		isRunning = true;
		try {
			if (sendMessage(new XmlMessageConnect()).getID() != Result.OK) {
				exit(sConnectionFailed);
			}
			Scanner sc = new Scanner(System.in);
			while (isRunning) {
				try {
					System.out.println(sHelp);

					byte command = translateCommand(sc.nextLine());

					if (!Message.isValid(command)) {
						System.err.println(sUnknownCommand);
						continue;
					}
					try {
						processCommand(command);
					}
					catch (SocketException e){
						exit(sLostConnection);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
