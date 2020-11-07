package server;

import protocol.Config;

import java.net.*;
import java.util.Scanner;


public class Server extends Thread {

	private int port = 8888;

	private boolean isRunning = false;

	public Server(int PORT){
		this.port = PORT;
	}

	public int getPort() {
		return port;
	}

	@Override
	public synchronized void start() {
		super.start();
	}

	public synchronized void start(boolean waitForTerminate){
		start();
		if (waitForTerminate)
			waitForTerminate();
	}

	@Override
	public void run() {
		try (ServerSocket server = new ServerSocket(port)) {
			isRunning = true;
			while (isRunning) {
				try {
					Socket socket = server.accept();
					new ServerThread(socket).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void waitForTerminate() {
		System.out.println("Server started\nCtrl+c to terminate");
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()){ }
		isRunning = false;
		interrupt();
	}

	public static void main(String[] args) {
		Server s = new Server(Config.PORT);
		s.start(true);
	}
}

