package client;

import message.Message;
import message.MessageResult;
import message.connection.MessageConnect;
import message.connection.MessageDisconnect;
import message.MessageException;
import message.menu.MessageMenuResult;
import message.order.MessageOrder;
import protocol.Config;
import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.Result;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread{

	private static final String helpString =
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

	private MessageResult sendMessage(Message msg) throws IOException,ClassNotFoundException {
		oos.writeObject(msg);
		return (MessageResult)ois.readObject();
	}

	private byte translateCommand(int cmd) throws Exception{
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

	private void processCommand(byte command) throws Exception {
		oos.writeObject(new Message(command));
		Object result = ois.readObject();
		Scanner sc = new Scanner(System.in);
		switch (command){
			case Command.MENU:
				System.out.println("Меню:");
				for (String line : ((MessageMenuResult)result).getOptions()){
					System.out.println(line);
				}
				break;
			case Command.ORDER:
				System.out.println("Введите адресс доставки:");
				String address = sc.nextLine();
				System.out.println("Выберите блюда из списка:");
				String choice = sc.nextLine();
				oos.writeObject(new MessageOrder(address,choice));
				break;
			case Command.END:
				disconnect();
				System.exit(0);
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
				System.out.println("Не удалось подключиться к серверу");
				System.exit(1);
			}
			while (isRunning) {
				try {
					System.out.println(helpString);

					byte command = translateCommand(System.in.read());
					if (!Message.isValid(command)) {
						System.err.println("Неизвестная команда");
						continue;
					}
					processCommand(command);
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
			System.out.println("Удачного дня");
		else
			System.out.println("Потеряно соединение сервером");
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
		new Client(new Socket(Config.HOST, Config.PORT)).run();
	}
}
