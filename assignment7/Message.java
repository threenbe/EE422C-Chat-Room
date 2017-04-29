package assignment7;

import java.io.Serializable;
/**
 * A Message stores the chatroom the text was sent in,
 * the user that sent it, and of course the actual text.
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private int chatroomNum;
	private int userNum;
	private String msg;
	// Constructor
	public Message(int cn, int un, String mg) {
		setChatroomNum(cn);
		setUserNum(un);
		setMsg(mg);
	}
	// These functions are self-explanatory
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
