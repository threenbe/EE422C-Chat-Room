package assignment7;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends PrintWriter implements Observer {
	public ClientObserver(OutputStream out) {
		super(out);
	}
	
	public void update(Observable obs, Message message) {
		this.println("User " + Integer.toString(message.getUserNum()) + " said: " + message.getMsg());
		this.flush();
	}

	@Override
	public void update(Observable obs, Object obj) {
		this.println(obj);
		this.flush();
	}
}
