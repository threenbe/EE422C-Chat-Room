package assignment7;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientMain extends Application{
	private BufferedReader reader;
	private PrintWriter writer;
	
	public static void main(String[] args) {
		ClientMain client = new ClientMain();
		// set up JavaFX window
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket("127.0.0.1", 5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		GridPane gridPane = new GridPane();
	}
}
