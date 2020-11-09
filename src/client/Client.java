package client;

import message.Message;
import message.MessageResult;
import message.connection.MessageConnect;
import message.connection.MessageDisconnect;
import message.MessageException;
import message.menu.MessageMenu;
import message.menu.MessageMenuResult;
import message.order.MessageOrder;
import message.order.MessageOrderResult;
import protocol.Config;
import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.Result;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client extends Thread{

	private static final String sLostConnection = "Потеряно соединение с сервером";
	private static final String sUnknownCommand = "Неизвестная команда";
	private static final String sResponseError = "Не удалось получить ответ от сервера";
	private static final String sConnectionFailed = "Не удалось подключиться к серверу";
	private static final String sMenu = "Меню:";
	private static final String sOrderNumber = "Номер вашего заказа: ";
	private static final String sInputAddress = "Введите адресс доставки:";
	private static final String sInputOrder = "Выберите блюда из списка:";
	private static final String sHaveANiceDay = "Удачного дня";
	private static final String sExit = "<Press ENTER to exit>";
	private static final String sHelp =
					"_____________________\n" +
					"| 1. Меню и цены    |\n" +
					"| 2. Сделать заказ  |\n" +
					"| 3. Выйти          |\n" +
					"|___________________|";

	private boolean isRunning;
	private final Socket clientSocket;
	private final ObjectInputStream ois;
	private final ObjectOutputStream oos;

	public Client(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		this.clientSocket.setSoTimeout(10000);
		oos = new ObjectOutputStream(this.clientSocket.getOutputStream());
		ois = new ObjectInputStream(this.clientSocket.getInputStream());
	}

	private void exit(String exitMessage) throws IOException{
		System.err.println(exitMessage);
		System.out.println(sExit);
		System.in.read();
		System.exit(1);
	}

	private MessageResult sendMessage(Message msg) throws IOException,ClassNotFoundException {
		oos.writeObject(msg);
		return (MessageResult)ois.readObject();
	}

	private byte translateCommand(int cmd) throws Exception{
		System.out.println(cmd);
		switch (cmd){
			case 1:
				return Command.MENU;
			case 2:
				return Command.ORDER;
			case 3:
				return Command.END;
			default:
				return Command.INVALID;
		}
	}

	private void readMenu() throws MessageException, IOException,ClassNotFoundException{
		oos.writeObject(new MessageMenu());
		MessageMenuResult result = (MessageMenuResult)ois.readObject();
		if (!result.checkError()) {
			System.out.println(sMenu);
			for (String line : result.getOptions()) {
				System.out.println(line);
			}
		}
		else {
			System.out.println(sResponseError);
		}
	}

	private void makeOrder() throws MessageException, IOException, ClassNotFoundException{
		Scanner sc = new Scanner(System.in);
		System.out.println(sInputAddress);
		String address = sc.nextLine();
		System.out.println(sInputOrder);
		String choice = sc.nextLine();
		oos.writeObject(new MessageOrder(address, choice));
		MessageOrderResult result = (MessageOrderResult)ois.readObject();
		if (!result.checkError())
			System.out.println(sOrderNumber + result.getNumber());
		else
			System.out.println(sResponseError);
	}

	private void processCommand(byte command) throws MessageException,IOException, ClassNotFoundException {
		switch (command){
			case Command.MENU:
				readMenu();
				break;

			case Command.ORDER:
				makeOrder();
				break;

			case Command.END:
				disconnect();
				exit("\n" + sExit);
				break;

			default:
				throw new CommandException(command);
		}
	}

	@Override
	public void run() {
		isRunning = true;
		try {
			if (sendMessage(new MessageConnect()).getResult() != Result.OK) {
				exit(sConnectionFailed);
			}
			Scanner sc = new Scanner(System.in);
			while (isRunning) {
				try {
					System.out.println(sHelp);

					byte command = translateCommand(sc.nextInt());

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

	private void disconnect() throws IOException, MessageException,ClassNotFoundException {
		isRunning = false;
		if (sendMessage(new MessageDisconnect()).getResult() == Result.OK)
			System.out.println(sHaveANiceDay);
		else
			System.out.println(sLostConnection);
		ois.close();
		oos.flush();
		oos.close();
		clientSocket.close();
	}

	@Override
	public void interrupt() {
		try {
			disconnect();
		}
		catch (Throwable e){ }
	}

	@Override
	protected void finalize() throws Throwable {
		disconnect();
	}

	public static void main(String[] args) throws Exception{
		new Client(new Socket(Config.HOST, Config.PORT)).start();
	}
}
