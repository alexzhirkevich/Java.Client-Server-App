package manage;
import client.Client;
import message.Message;
import message.MessageException;
import protocol.command.Command;
import protocol.command.CommandException;
import protocol.result.Result;
import protocol.result.ResultException;
import server.Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.module.ResolutionException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Manage {

	private static final String helpString = "Syntax:\n" + "run {server/client}\n";

	private static void error() {
		System.err.println(helpString);
		System.exit(1);
	}

	public static void main(String[] args) throws IOException, ConnectException {
//		try {
//			if (args.length != 2) {
//				error();
//			} else {
//				if (args[0].equals("run")) {
//					if (args[1].equals("server")) {
//						Server s =new Server(8888);
//						s.start();
//						s.waitForTerminate();
//					} else if (args[1].equals("client")) {
//						new Client(new Socket("localhost", 8888)).start();
//					} else
//						error();
//				} else
//					error();
//			}
//		} catch (ConnectException e){
//			System.err.println("Server is offline");
//		}
//		catch (FileNotFoundException e){
//			System.err.println(e.getMessage());
//		}
//		catch (Exception e){
//			e.printStackTrace(System.err);
//		}
//
//		finally {
//			new Scanner(System.in).next();
//		}
		try {
			throw new ResultException(Result.INVALID);
		}
		catch (MessageException e){
			e.printStackTrace();
		}
	}
}