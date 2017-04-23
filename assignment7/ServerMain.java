package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class ServerMain extends Observable {
	public static void main(String[] args) {
		try {
			ServerMain server = new ServerMain();
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket();
			while (true) {
				// make a new thread for managing each client connection
				Socket clientSocket = serverSocket.accept();
				ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
				new Thread(new Runnable(){
					@Override
					public void run() {
						// TODO Add all the shit for handling a client here AFAIK
						String message;
						try {
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(clientSocket.getInputStream()));
							message = reader.readLine();
							while (message != null) {
								System.out.println("message was read by server");
								server.setChanged();
								server.notifyObservers(message);
								message = reader.readLine();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
				server.addObserver(writer);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}