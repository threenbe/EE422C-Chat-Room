package assignment7;

import java.io.BufferedInputStream;
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
	private static HashMap<String, Integer> passwordList;
	private static ArrayList<User> users;
	private static ArrayList<Chatroom> chatrooms;
	private static ArrayList<ClientObserver> observers;
	
	private int usersCount = -1;
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
		passwordList = new HashMap<String, Integer>();
		users = new ArrayList<User>();
		chatrooms = new ArrayList<Chatroom>();
		observers = new ArrayList<ClientObserver>();
		
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
						//		new InputStreamReader(clientSocket.getInputStream()));
						ObjectInputStream reader = new ObjectInputStream(
								new BufferedInputStream(clientSocket.getInputStream()));
						//message = reader.readLine();
						message = reader.readObject();
						while (message != null) {
							processMessage(message, writer); 
							//message = reader.readLine();
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
	private void processMessage(Object message, ClientObserver writer) {
		// TODO eventually take this out
		if (message instanceof String) {
			try {
				String msg = (String) message;
				String[] msg_split = msg.split(" ");
				if (msg_split[0].equals("/SIGNUP")) {
					for (User u : users) {
						if (u.getName().equals(msg_split[1])) {
							writer.writeObject("name-taken ");
							return;
						}
					}
					users.add(new User(++usersCount, msg_split[1]));
					passwordList.put(msg_split[2], usersCount);
					observers.add(usersCount, writer);
					writer.writeObject("registered " + usersCount + " " + msg_split[1]);
					//writer.println("registered " + usersCount + " " + msg_split[1]);
					writer.flush();
					return;
				} else if (msg_split[0].equals("/SIGNIN")) {
					observers.add(usersCount, writer);
					writer.writeObject("logged-in ");
					//writer.println("registered");
					writer.flush();
				}
				setChanged();
				notifyObservers(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		} else if (message instanceof Message) {
			Message msg = (Message) message;
			String[] tokens = msg.getMsg().split(" ");
			if (tokens[0].equals("/createChatroom")) {
				Chatroom cr = new Chatroom(chatroomsCount, "Chatroom #" + chatroomsCount, "");
				cr.addMember(msg.getUserNum());
				for (int i = 1; i < tokens.length; i++) {
					int id = getUserId(tokens[i]);
					if (id >= 0) cr.addMember(id);
				}
				chatrooms.add(chatroomsCount, cr);
				chatroomsCount++;
			} else if (tokens[0].equals("/addMember") || tokens[0].equals("/addMembers")) {
				int user;
				for (int i = 1; i < tokens.length; i++) {
					user = getUserId(tokens[i]);
					if (user >= 0) chatrooms.get(msg.getChatroomNum()).addMember(user);
				}
			} else if (tokens[0].equals("/changeChatroomName")) {
				chatrooms.get(msg.getChatroomNum()).setName(tokens[1]);
				// TODO make sure users in chatroom see this update
			} else if (tokens[0].equals("/changeNick")) {
				users.get(msg.getUserNum()).setName(tokens[1]);
				// TODO make sure chatrooms and users see this update
			} else if (tokens[0].equals("/addFriend")) {
				int id = getUserId(tokens[1]);
				users.get(msg.getUserNum()).addFriend(id);
				// TODO make sure users see this update
			} else { // just plain old message
				chatrooms.get(msg.getChatroomNum()).sendMessage(msg);
				//setChanged();
				//notifyObservers(msg);
			}
		} else {
			System.out.println("Unfamiliar object type inputted by client. Input ignored. Fix later.");
		}
	}
	public int getUserId(String name) {
		for (User u : users) {
			if (u.getName().equals(name)) {
				return u.getUserNum();
			}
		}
		return -1;
	}
	public static String getUserName(int id) {
		if (users.size() <= id) return null;
		return users.get(id).getName();
	}
	
	public static ClientObserver getObserver(int id) {
		return observers.get(id);
	}
}