package application.manage;

import application.objectstream.client.Client;
import application.objectstream.server.Server;
import application.protocol.Config;
import application.xml.client.XmlClient;
import application.xml.schema.generator.XsdGenerator;
import application.xml.server.XmlServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Manage {

	private static final String helpString = "Syntax:\n" +
			"shema / runserver / runclient / runserverxml / runclientxml";

	private static void error() {
		System.err.println(helpString);
		System.exit(1);
	}

	public static void schema() {
		try {
			XsdGenerator.generateAll();
			System.out.println("Success");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error");
		}
		System.out.println("Press <ENTER> to exit");
	}

	private static void createDirectories() throws IOException {
		Files.createDirectories(Paths.get(Config.orderDir));
		Files.createDirectories(Paths.get(Config.dtdDirClient));
		Files.createDirectories(Paths.get(Config.dtdDirServer));
		Files.createDirectories(Paths.get(Config.xsdDirClient));
		Files.createDirectories(Paths.get(Config.xsdDirClient));
	}

	public static void main(String[] args) {
		try {
			if (args.length != 1)
				error();

			createDirectories();

			switch (args[0]) {
				case "schema":
					schema();
					break;
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

		} catch (ConnectException e) {
			System.err.println("Server is offline");
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			new Scanner(System.in).nextLine();
		}
	}
}