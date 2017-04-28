package assignment7;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Observable;

public class ServerMain extends Observable {
	private static ArrayList<User> users;
	private static ArrayList<String> passwords;
	private static ArrayList<Chatroom> chatrooms;
	private static ArrayList<ClientObserver> observers;
	
	private int usersCount = 0;
	private int chatroomsCount = 1;
	
	public static void main(String[] args) {
		try {
			new ServerMain().setUpNetworking();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		// Initializations
		users = new ArrayList<User>();
		chatrooms = new ArrayList<Chatroom>();
		observers = new ArrayList<ClientObserver>();
		passwords = new ArrayList<String>();
		chatrooms.add(0, new Chatroom(0, "Global", ""));
		
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(5000);
		while (true) {
			// Makes a new thread for each client connection
			Socket clientSocket = serverSocket.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			new Thread(new Runnable(){
				@Override
				public void run() {
					Object message;
					try {
						ObjectInputStream reader = new ObjectInputStream(
								new BufferedInputStream(clientSocket.getInputStream()));
						// Constantly checks for messages
						while ((message = reader.readObject()) != null) {
							processMessage(message, writer); 
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
					int userNum = usersCount++;
					User this_user = new User(userNum, msg_split[1]);
					this_user.setOnline(true);
					users.add(this_user);
					passwords.add(msg_split[2]);
					observers.add(userNum, writer);
					chatrooms.get(0).addMember(userNum);
					writer.writeObject("registered " + userNum + " " + msg_split[1]);
					writer.flush();
					return;
				} else if (msg_split[0].equals("/SIGNIN")) {
					String this_name = msg_split[1];
					String this_password = msg_split[2];
					int id = getUserId(this_name);
					if (id == -1) {
						writer.writeObject("name-not-found ");
						return;
					}
					if (this_password.equals(passwords.get(id))) {
						User u = users.get(id);
						if (u.isOnline()) {
							writer.writeObject("already-online ");
							return;
						} else {
							u.setOnline(true);
							for (Chatroom c : chatrooms) {
								if (c.isMember(id)) {
									c.addObserver(writer);
									c.sendChatroom();
								}
							}
							observers.set(id, writer);
							writer.writeObject("logged-in " + id + " " + this_name);
							writer.flush();
							return;
						}
						/*for (User u : users) {
							if (u.getName().equals(this_name)) {
								if (u.isOnline()) {
									writer.writeObject("already-online ");
									return;
								} else {
									u.setOnline(true);
								}
							}
						}*/
					} else {
						writer.writeObject("wrong-password ");
						return;
					}
				} else if (msg_split[0].equals("/LOGOUT")) {
					for (User u : users) {
						if (u.getName().equals(getUserName(Integer.parseInt(msg_split[1])))) {
							u.setOnline(false);
							int id = u.getUserNum();
							ClientObserver obs = observers.get(id);
							if (obs != null) {
								for (Chatroom c : chatrooms) {
									c.deleteObserver(obs);
								}
								this.deleteObserver(obs);
							}
							observers.set(id, null);
							
							return;
						}
					}
				} else if (msg_split[0].equals("/getData")) {
					if (msg_split[1].equals("chatroom")) {
						writer.writeObject(chatrooms.get(Integer.parseInt(msg_split[2])));
					} else if (msg_split[1].equals("user")) {
						writer.writeObject(users.get(Integer.parseInt(msg_split[2])));
					}
					return;
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
				cr.sendChatroom();
			} else if (tokens[0].equals("/addMember") || tokens[0].equals("/addMembers")) {
				int user;
				Chatroom cr = chatrooms.get(msg.getChatroomNum());
				for (int i = 1; i < tokens.length; i++) {
					user = getUserId(tokens[i]);
					if (user >= 0) cr.addMember(user);
				}
				cr.sendChatroom();
			} else if (tokens[0].equals("/changeChatroomName")) {
				Chatroom cr = chatrooms.get(msg.getChatroomNum());
				cr.setName(tokens[1]);
				cr.sendChatroom();
			} else if (tokens[0].equals("/changeNick")) {
				users.get(msg.getUserNum()).setName(tokens[1]);
				// TODO make sure chatrooms and users see this update
			} else if (tokens[0].equals("/addFriend")) {
				int id = getUserId(tokens[1]);
				if (id >= 0) users.get(msg.getUserNum()).addFriend(id);
				// TODO make sure users see this update
			} else { // just plain old message
				chatrooms.get(msg.getChatroomNum()).sendMessage(msg);
			}
		} else {
			System.out.println("Unfamiliar object type input by client. Input ignored. Fix later.");
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
	public static String getChatroomName(int id) {
		if (chatrooms.size() <= id) return null;
		return chatrooms.get(id).getName();
	}
	public static ClientObserver getObserver(int id) {
		return observers.get(id);
	}
}