package application.manage;

import application.objectstream.client.Client;
import application.protocol.*;
import application.objectstream.server.Server;
import application.xml.client.XmlClient;
import application.xml.server.XmlServer;
import application.xml.server.XmlServerThread;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Scanner;

public class Manage {

	private static final String helpString = "Syntax:\n" +
			"runserver / runclient / runserverxml / runclientxml";

	private static void error() {
		System.err.println(helpString);
		System.exit(1);
	}

	public static void runServer(String[] args){
		switch (args.length){
			case 2:
				new Server(Config.PORT).start(true);
				break;
			case 3:
				try {
					new Server(Integer.parseInt(args[2])).start(true);
				} catch (NumberFormatException e) { error(); }
				break;
			default:
				error();
		}
	}

	public static void runClient(String[] args) throws IOException{
		switch (args.length) {
			case 2:
				new Client(Config.HOST, Config.PORT).start();
				break;
			case 4:
				try {
					new Client(args[2], Integer.parseInt(args[3])).start();
				} catch (NumberFormatException e) { error(); }
				break;
			default:
				error();
		}
	}

	public static void run(String[] args) throws IOException {
		switch (args[1]){
			case "server":
				runServer(args);
				break;
			case "client":
				runClient(args);
				break;
			default:
				error();
		}
	}

	public static void main(String[] args) {
		try {
			if (args.length != 1)
				error();
			switch (args[0]){
				case "runserver":
					new Server(Config.PORT).start(true);
					break;
				case "runclient":
					new Client(Config.HOST, Config.PORT).start();
					break;
				case "runserverxml":
					new XmlServer(Config.PORT).start(true);
					break;
				case "runclientxml":
					new XmlClient(Config.HOST, Config.PORT).start();
					break;
				default:
					error();
			}

		} catch (ConnectException e){
			System.err.println("Server is offline");
		}
		catch (FileNotFoundException e){
			System.err.println(e.getMessage());
		}
		catch (Exception e){
			e.printStackTrace(System.err);
		}
		finally {
			new Scanner(System.in).next();
		}
	}
}