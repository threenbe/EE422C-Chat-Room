package assignment7;

import java.util.ArrayList;
import java.util.Observable;

public class Chatroom extends Observable {
	private int chatroomNum;
	private String name;
	private String password;
	private ArrayList<Message> history = new ArrayList<Message>();
	private ArrayList<Integer> members = new ArrayList<Integer>();
	
	public Chatroom(int num, String nm, String pw) {
		chatroomNum = num;
		setName(nm);
		setPassword(pw);
	}
	
	
	
	public void sendMessage(Message message) {
		setChanged();
		notifyObservers(message);
	}
	public void addMember(int member) {
		members.add(member);
		this.addObserver(ServerMain.getObserver(member));
	}
	public void removeMember(int member) {
		members.remove(member);
		this.deleteObserver(ServerMain.getObserver(member));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<Message> getHistory() {
		return history;
	}
	public void addHistory(Message msg) {
		history.add(msg);
	}
}
