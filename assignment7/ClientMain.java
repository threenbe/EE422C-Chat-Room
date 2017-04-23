package assignment7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ClientMain extends Application {
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		ClientMain client = new ClientMain();
		// set up JavaFX window
		// GridPane for all the buttons and text boxes
		GridPane gridPane = new GridPane();
		
		// area for text
		Label text = new Label();
		text.setPrefWidth(350);
		text.setTextFill(Color.BLACK);
		gridPane.add(text, 0, 10);
		
		// area for text
		Label input = new Label();
		input.setPrefWidth(350);
		input.setTextFill(Color.BLACK);
		gridPane.add(input, 0, 11);
		
		// box to input message
		TextField msgInput = new TextField();
		msgInput.setPromptText("Enter your message here");
		msgInput.setPrefWidth(350);
		gridPane.add(msgInput, 0, 0);
		
		// button to send message
		Button send = new Button();
		send.setText("Send");
		gridPane.add(send, 1, 0);
		send.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	try {
		    		// TODO
		    		client.writer.println(msgInput.getText());
		    		client.writer.flush();
		    		msgInput.setText("");
		    		text.setText("Message sent.");
	    		} catch (Exception f){
	    			text.setText("Error sending message!");
	    		}
		    }
		});
		
		
		// sets up connection with server
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket("127.0.0.1", 5000);
			client.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			client.writer = new PrintWriter(socket.getOutputStream());
			System.out.println("connected");
			new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String message;
					try {
						message = client.reader.readLine();
						while (message != null) {
							// TODO make this append instead
							final String msg = message;
							Platform.runLater(new Runnable(){
								@Override
								public void run() {
									input.setText(msg);
								}
							});
							
							message = client.reader.readLine();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		primaryStage.setScene(new Scene(gridPane, 560, 700));
		primaryStage.show();
	}
}
