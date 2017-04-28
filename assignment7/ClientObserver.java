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
		try{
			/*String message = "";
			if (obj instanceof Message) {
				Message input = (Message) obj;
				message = "User " + ServerMain.getUserName(input.getUserNum()) + " said: " + input.getMsg();
				this.writeObject(input);
				//this.println(message);
				this.flush();
			} else if (obj instanceof String) {
				this.writeObject(obj);
				//this.println(obj);
				this.flush();
			}*/
			this.reset();
			this.writeObject(obj);
			this.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
