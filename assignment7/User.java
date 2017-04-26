package assignment7;

import java.util.ArrayList;

public class User {
	private int userNum;
	private String name;
	private ArrayList<User> friends = new ArrayList<User>();
	
	public User(int num, String nm) {
		setUserNum(num);
		setName(nm);
	}
	
	
	
	public void addFriend(User friend) {
		friends.add(friend);
	}
	
	public ArrayList<User> getFriends() {
		return friends;
	}
	public void setFriends(ArrayList<User> friends) {
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
}
