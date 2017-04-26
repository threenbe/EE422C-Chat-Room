package assignment7;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private int chatroomNum;
	private int userNum; // user that sent the message
	private String msg;
	
	public Message(int cn, int un, String mg) {
		setChatroomNum(cn);
		setUserNum(un);
		setMsg(mg);
	}

	public int getChatroomNum() {
		return chatroomNum;
	}
	public void setChatroomNum(int chatroomNum) {
		this.chatroomNum = chatroomNum;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
