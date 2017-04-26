package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class ServerMain extends Observable {
	private HashMap<Integer, User> passwordList;
	private ArrayList<User> users;
	private ArrayList<Chatroom> chatrooms;
	
	private int usersCount = 0;
	private int chatroomsCount = 0;
	
	public static void main(String[] args) {
		try {
			new ServerMain().setUpNetworking();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		// other inits
		passwordList = new HashMap<Integer, User>();
		users = new ArrayList<User>();
		chatrooms = new ArrayList<Chatroom>();
		
		
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(5000);
		while (true) {
			// make a new thread for managing each client connection
			Socket clientSocket = serverSocket.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Add all the shit for handling a client here AFAIK
					//String message;
					Object message;
					try {
						//BufferedReader reader = new BufferedReader(
								//new InputStreamReader(clientSocket.getInputStream()));
						ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
						//message = reader.readLine();
						message = reader.readObject();
						while (message != null) {
							processMessage(message); // TODO
							message = reader.readObject();
						}
					} catch (SocketException e) {
						System.out.println("Client disconnected!");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}).start();
			this.addObserver(writer);
		}
	}
	private void processMessage(/*String*/ Object message) {
		setChanged();
		notifyObservers(message);
	}
}