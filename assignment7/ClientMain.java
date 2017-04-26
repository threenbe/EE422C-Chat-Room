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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientMain extends Application {
	
	private BufferedReader reader;
	private PrintWriter writer;
	private User user;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientMain client = new ClientMain();
		// set up JavaFX window
		// pane for all the buttons and text boxes
		Pane pane = new Pane();
		
		// area for text
		Label text = new Label();
		text.setPrefWidth(350);
		text.setLayoutX(0);
		text.setLayoutY(670);
		text.setTextFill(Color.BLACK);
		pane.getChildren().add(text);
		text.setVisible(false);
		
		// area for text
		Label input = new Label();
		input.setPrefWidth(350);
		input.setLayoutX(0);
		input.setLayoutY(0);
		input.setTextFill(Color.BLACK);
		pane.getChildren().add(input);
		input.setVisible(false);
		
		// box to input message
		TextField msgInput = new TextField();
		msgInput.setPromptText("Enter your message here");
		msgInput.setPrefWidth(350);
		msgInput.setLayoutX(0);
		msgInput.setLayoutY(640);
		pane.getChildren().add(msgInput);
		msgInput.setVisible(false);
		
		// button to send message
		Button send = new Button();
		send.setText("Send");
		send.setLayoutX(350);
		send.setLayoutY(640);
		pane.getChildren().add(send);
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
		send.setVisible(false);
		
		TextField enterNameField = new TextField();
		enterNameField.setPrefWidth(160);
		enterNameField.setLayoutX(200);
		enterNameField.setLayoutY(300);
		pane.getChildren().add(enterNameField);
		Text namePrompt = new Text();
		namePrompt.setLayoutX(135);
		namePrompt.setLayoutY(320);
		namePrompt.setFont(Font.font("Verdana", 18));
		namePrompt.setText("Name: ");
		pane.getChildren().add(namePrompt);
		
		PasswordField enterPasswordField = new PasswordField();
		enterPasswordField.setPrefWidth(160);
		enterPasswordField.setLayoutX(200);
		enterPasswordField.setLayoutY(350);
		pane.getChildren().add(enterPasswordField);
		Text passwordPrompt = new Text();
		passwordPrompt.setLayoutX(105);
		passwordPrompt.setLayoutY(370);
		passwordPrompt.setFont(Font.font("Verdana", 18));
		passwordPrompt.setText("Password: ");
		pane.getChildren().add(passwordPrompt);
		
		Button signIn = new Button();
		signIn.setText("Sign In");
		signIn.setPrefWidth(100);
		signIn.setPrefHeight(20);
		signIn.setLayoutX(230);
		signIn.setLayoutY(390);
		pane.getChildren().add(signIn);
		signIn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String name = enterNameField.getText();
				String password = enterNameField.getText();
				if (!name.equals("") && !password.equals("")) {
					client.writer.println("/SIGNIN " + name + " " + password);
					client.writer.flush();
				}
				enterNameField.clear();
				enterPasswordField.clear();
			}
			
		});
		
		Button registerBtn = new Button();
		registerBtn.setText("Sign Up");
		registerBtn.setPrefWidth(100);
		registerBtn.setPrefHeight(20);
		registerBtn.setLayoutX(230);
		registerBtn.setLayoutY(420);
		pane.getChildren().add(registerBtn);
		registerBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String name = enterNameField.getText();
				String password = enterNameField.getText();
				if (!name.equals("") && !password.equals("")) {
					client.writer.println("/SIGNUP " + name + " " + password);
					client.writer.flush();
				}
				enterNameField.clear();
				enterPasswordField.clear();
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
						while ((message = client.reader.readLine()) != null) {
							String[] split_msg = message.split(" ");
							if (split_msg[0].equals("registered")) {
								user = new User(Integer.parseInt(split_msg[1]), split_msg[2]);
								registerBtn.setVisible(false);
								signIn.setVisible(false);
								enterPasswordField.setVisible(false);
								enterNameField.setVisible(false);
								namePrompt.setVisible(false);
								passwordPrompt.setVisible(false);
								send.setVisible(true);
								msgInput.setVisible(true);
								input.setVisible(true);
								text.setVisible(true);
							} else if (split_msg[0].equals("logged-in")) {
								registerBtn.setVisible(false);
								signIn.setVisible(false);
								enterPasswordField.setVisible(false);
								enterNameField.setVisible(false);
								namePrompt.setVisible(false);
								passwordPrompt.setVisible(false);
								send.setVisible(true);
								msgInput.setVisible(true);
								input.setVisible(true);
								text.setVisible(true);
							} else {
								// TODO make this append instead
								final String msg = message;
								Platform.runLater(new Runnable(){
									@Override
									public void run() {
										input.setText(msg);
									}
								});
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		primaryStage.setScene(new Scene(pane, 560, 700));
		primaryStage.show();
	}
}
