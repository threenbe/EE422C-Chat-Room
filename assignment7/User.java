package assignment7;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private int userNum;
	private String name;
	private boolean isOnline;
	private ArrayList<Integer> friends = new ArrayList<Integer>();
	//private ArrayList<User> friends = new ArrayList<User>();
	
	public User(int num, String nm) {
		setUserNum(num);
		setName(nm);
	}
	
	
	
	public void addFriend(int friend) {
		friends.add(friend);
	}
	
	public ArrayList<Integer> getFriends() {
		return friends;
	}
	public void setFriends(ArrayList<Integer> friends) {
		this.friends = friends;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
}
