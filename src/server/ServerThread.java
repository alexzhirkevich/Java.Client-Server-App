package server;

import message.Message;
import message.MessageException;
import message.MessageResult;
import message.connection.MessageConnectResult;
import message.connection.MessageDisconnectResult;
import message.menu.MessageMenuResult;
import message.order.MessageOrder;
import message.order.MessageOrderResult;
import protocol.command.Command;
import protocol.result.Result;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerThread extends Thread {

	private static final String fileName = "menu.txt";
	private static Integer orderNum = 1;

	private boolean isRunning;
	private final Socket client;
	private final ObjectOutputStream oos;
	private final ObjectInputStream ois;
	private final InetAddress ia;

	public ServerThread(Socket s) throws IOException {
		this.client = s;
		client.setSoTimeout(500);
		oos = new ObjectOutputStream(client.getOutputStream());
		ois = new ObjectInputStream(client.getInputStream());
		ia = client.getInetAddress();
	}

	public InetAddress getClientInetAddress() {
		return ia;
	}

	private static synchronized String[] readMenu(String fName) throws IOException {
		ArrayList<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fName), StandardCharsets.UTF_8))) {
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		}
		return lines.toArray(new String[0]);
	}

	private synchronized MessageOrderResult saveOrder(MessageOrder order) throws MessageException {
		try {
			String orderName = "orders\\" + (orderNum).toString() + ".txt";
			File f = new File(orderName);
			if (!f.exists())
				f.createNewFile();

			try (BufferedWriter br = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(fileName)))) {
				br.write(order.getAddress() + '\n' + order.getOrder());
				br.flush();
			}
			return new MessageOrderResult(Result.OK, orderNum++);
		} catch (IOException e) {
			return new MessageOrderResult(Result.IO_ERROR, -1);
		} catch (MessageException e) {
			return new MessageOrderResult(Result.MSG_ERROR, -1);
		} catch (Exception e) {
			return new MessageOrderResult(Result.UNKNOWN_ERROR, -1);
		}
	}

	private MessageResult processMessage(Message msg) throws
			IOException, ClassNotFoundException, MessageException {

		if (msg == null)
			return null;

		System.out.println("New message. From " + ia.getHostAddress() + ". Command: " + msg.getId());

		switch (msg.getId()) {
			case Command.START:
				System.out.println("Connected: " + ia.getHostAddress());
				return new MessageConnectResult(Result.OK);
			case Command.MENU:
				return new MessageMenuResult(Result.OK, readMenu(fileName));
			case Command.ORDER: {
				MessageOrder order = (MessageOrder) ois.readObject();
				MessageOrderResult orderResult = saveOrder(order);
				if (orderResult.checkError()) {
					System.out.println("New order. #" + orderResult.getNumber());
					orderNum++;
				}
				return orderResult;
			}
			case Command.END:
				try {
					isRunning = false;
					oos.writeObject(new MessageDisconnectResult(Result.OK));
					ois.close();
					oos.close();
					client.close();
					System.out.println("Disconnected: " + ia.getHostAddress());
					return null;
				} catch (IOException e) {
					return new MessageDisconnectResult(Result.IO_ERROR);
				} catch (Exception e) {
					return new MessageDisconnectResult(Result.UNKNOWN_ERROR);
				}
			default:
				return new MessageResult(Command.INVALID, Result.UNKNOWN_ERROR);
		}
	}

	@Override
	public void run() {
		isRunning = true;
		while (isRunning) {
			Message msg = null;
			try {
				msg = (Message) ois.readObject();
			} catch (IOException | ClassNotFoundException e) { }

			try {
				MessageResult result = processMessage(msg);
				if (result != null)
					oos.writeObject(result);
			} catch (IOException e) {
				try {
					if (msg != null)
						oos.writeObject(new MessageResult(msg.getId(), Result.IO_ERROR));
					e.printStackTrace();
				} catch (IOException | MessageException err) { }
			} catch (ClassNotFoundException | MessageException e) { }
		}
	}
}