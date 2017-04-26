package assignment7;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends ObjectOutputStream /*PrintWriter*/ implements Observer {
	public ClientObserver(OutputStream out) throws IOException {
		super(out);
	}

	@Override
	public void update(Observable obs, Object obj) {
		String message = "";
		if (obj instanceof Message) {
			message = "User " + Integer.toString(((Message)obj).getUserNum()) + " said: " + ((Message)obj).getMsg();
			//this.println(message);
			try {
				this.writeObject(message);
				this.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
