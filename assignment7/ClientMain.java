package assignment7;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientMain extends Application {
	
	//private BufferedReader reader;
	private ObjectInputStream reader;
	//private PrintWriter writer;
	private ObjectOutputStream writer;
	private User user;
	private int userNum;
	private HashMap<Integer, ClientTab> chatrooms;
	private int chatroomCount = 0;
	private int currentChatroom = 0;
	private HashMap<Integer, Tab> tabs = new HashMap<Integer, Tab>();
	// experimental
	private Message temp;
	private Message tempMsg;
	
	// all JavaFX UI stuff. MAKE NEW ONES HERE
	Label text = new Label();
	//Label input = new Label();
	TextField msgInput = new TextField();
	Button send = new Button();
	TextField enterNameField = new TextField();
	Text namePrompt = new Text();
	PasswordField enterPasswordField = new PasswordField();
	Text passwordPrompt = new Text();
	Button signIn = new Button();
	Button registerBtn = new Button();
	Button logoutBtn = new Button();
	Text loginError = new Text();
	Pane pane = new Pane();
	TabPane tabPane = new TabPane();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientMain client = new ClientMain();
		chatrooms = new HashMap<Integer, ClientTab>();
		// set up JavaFX window
		
		// area for text
		//Label text = new Label();
		text.setPrefWidth(350);
		text.setLayoutX(0);
		text.setLayoutY(670);
		text.setTextFill(Color.BLACK);
		pane.getChildren().add(text);
		text.setVisible(false);
		
		// area for text
		//Label input = new Label();
		/*input.setPrefWidth(350);
		input.setLayoutX(0);
		input.setLayoutY(0);
		input.setTextFill(Color.BLACK);
		pane.getChildren().add(input);
		input.setVisible(false);*/
		
		// box to input message
		//TextField msgInput = new TextField();
		msgInput.setPromptText("Enter your message here");
		msgInput.setPrefWidth(350);
		msgInput.setLayoutX(0);
		msgInput.setLayoutY(640);
		pane.getChildren().add(msgInput);
		msgInput.setVisible(false);
		
		// button to send message
		//Button send = new Button();
		send.setText("Send");
		send.setLayoutX(350);
		send.setLayoutY(640);
		pane.getChildren().add(send);
		send.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	try {
		    		// TODO
		    		//client.writer.println(msgInput.getText());
		    		client.writer.writeObject(createMessage());
		    		client.writer.flush();
		    		msgInput.setText("");
		    		text.setText("Message sent.");
	    		} catch (Exception f){
	    			text.setText("Error sending message!");
	    		}
		    }
		});
		send.setVisible(false);
		
		logoutBtn.setText("Logout");
		logoutBtn.setLayoutX(350);
		logoutBtn.setLayoutY(670);
		pane.getChildren().add(logoutBtn);
		logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				registerBtn.setVisible(true);
				signIn.setVisible(true);
				enterPasswordField.setVisible(true);
				enterNameField.setVisible(true);
				namePrompt.setVisible(true);
				passwordPrompt.setVisible(true);
				send.setVisible(false);
				msgInput.setVisible(false);
				//input.setVisible(false);
				text.setVisible(false);
				logoutBtn.setVisible(false);
				loginError.setText("");
				loginError.setVisible(true);
				try {
					client.writer.writeObject("/LOGOUT " + userNum);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		logoutBtn.setVisible(false);
		
		//TextField enterNameField = new TextField();
		enterNameField.setPrefWidth(160);
		enterNameField.setLayoutX(200);
		enterNameField.setLayoutY(300);
		pane.getChildren().add(enterNameField);
		//Text namePrompt = new Text();
		namePrompt.setLayoutX(135);
		namePrompt.setLayoutY(320);
		namePrompt.setFont(Font.font("Verdana", 18));
		namePrompt.setText("Name: ");
		pane.getChildren().add(namePrompt);
		
		//PasswordField enterPasswordField = new PasswordField();
		enterPasswordField.setPrefWidth(160);
		enterPasswordField.setLayoutX(200);
		enterPasswordField.setLayoutY(340);
		pane.getChildren().add(enterPasswordField);
		//Text passwordPrompt = new Text();
		passwordPrompt.setLayoutX(105);
		passwordPrompt.setLayoutY(360);
		passwordPrompt.setFont(Font.font("Verdana", 18));
		passwordPrompt.setText("Password: ");
		pane.getChildren().add(passwordPrompt);
		
		//error when logging in or registering
		loginError.setLayoutX(125);
		loginError.setLayoutY(380);
		loginError.setFill(Color.RED);
		pane.getChildren().add(loginError);
		
		//Button signIn = new Button();
		signIn.setText("Sign In");
		signIn.setPrefWidth(100);
		signIn.setPrefHeight(20);
		signIn.setLayoutX(230);
		signIn.setLayoutY(390);
		pane.getChildren().add(signIn);
		signIn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					String name = enterNameField.getText();
					String password = enterPasswordField.getText();
					if (!name.equals("") && !password.equals("")) {
						//client.writer.println("/SIGNIN " + name + " " + password);
						client.writer.writeObject("/SIGNIN " + name + " " + password);
						client.writer.flush();
					}
					enterNameField.clear();
					enterPasswordField.clear();
				} catch (IOException f) {
					f.printStackTrace();
				}
			}
			
		});
		
		//Button registerBtn = new Button();
		registerBtn.setText("Sign Up");
		registerBtn.setPrefWidth(100);
		registerBtn.setPrefHeight(20);
		registerBtn.setLayoutX(230);
		registerBtn.setLayoutY(420);
		pane.getChildren().add(registerBtn);
		registerBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					String name = enterNameField.getText();
					String password = enterPasswordField.getText();
					if (!name.equals("") && !password.equals("")) {
						//client.writer.println("/SIGNUP " + name + " " + password);
						client.writer.writeObject("/SIGNUP " + name + " " + password);
						client.writer.flush();
					}
					enterNameField.clear();
					enterPasswordField.clear();
				} catch (IOException f) {
					f.printStackTrace();
				}
			}
			
		});
		
		// sets up connection with server
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket("127.0.0.1", 5000);
			//client.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			client.reader = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			//client.writer = new PrintWriter(socket.getOutputStream());
			client.writer = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("connected");
			new Thread(new Runnable(){
				@Override
				public void run() {
					Object message;
					//String message;
					try {
						while ((message = client.reader.readObject()/*readLine()*/) != null) {
							if (message instanceof Chatroom) {
								Chatroom cr = (Chatroom) message;
								Tab crTab = tabs.get(cr.getChatroomNum());
								if (crTab == null) {
									//createTab(cr);
									Tab tab = new Tab();
									tab.setText(cr.getName());
									TextArea textArea = new TextArea();
									tab.setContent(textArea);
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											tabs.put(cr.getChatroomNum(), tab);
											tabPane.getTabs().add(tab);
										}
									});
									
									crTab = tabs.get(cr.getChatroomNum());
								}
								if (temp != null) {
									Message msg = temp;
									temp = null;
									Tab tab = tabs.get(msg.getChatroomNum());
									if (tab == null) {
										temp = msg;
										client.writer.writeObject("/getData chatroom " + msg.getChatroomNum());
									} else {
										tempMsg = msg;
										client.writer.writeObject("/getData user " + msg.getUserNum());
									}
								}
							} else if (message instanceof Message) {
								System.out.println("message read");
								Message msg = (Message) message;
								//TODO
								Tab tab = tabs.get(msg.getChatroomNum());
								if (tab == null) {
									temp = msg;
									client.writer.writeObject("/getData chatroom " + msg.getChatroomNum());
								} else {
									tempMsg = msg;
									client.writer.writeObject("/getData user " + msg.getUserNum());
								}
							} else if (message instanceof User) {
								User user = (User) message;
								if (user.getUserNum() == tempMsg.getUserNum()) {
									String mesg = "User " + user.getName() + " said: " + tempMsg.getMsg();
									TextArea ta = (TextArea) tabs.get(tempMsg.getChatroomNum()).getContent();
									ta.appendText("\n" + mesg);
								}
							} else if (message instanceof String) {
								String msg = (String) message; 
								processString(msg);
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
		
		tabPane.setMinWidth(560);
		tabPane.setMinHeight(100);
		//tabPane.getTabs().add(new Tab("Ass cheese"));
		pane.getChildren().add(tabPane);
		
		
		primaryStage.setScene(new Scene(pane, 560, 700));
		primaryStage.show();
	}
	private void processString(String message) {
		String[] split_msg = ((String)message).split(" ");
		if (split_msg[0].equals("registered")) {
			user = new User(Integer.parseInt(split_msg[1]), split_msg[2]);
			userNum = user.getUserNum();
			registerBtn.setVisible(false);
			signIn.setVisible(false);
			enterPasswordField.setVisible(false);
			enterNameField.setVisible(false);
			namePrompt.setVisible(false);
			passwordPrompt.setVisible(false);
			send.setVisible(true);
			msgInput.setVisible(true);
			//input.setVisible(true);
			text.setVisible(true);
			logoutBtn.setVisible(true);
			loginError.setText("");
			loginError.setVisible(false);
		} else if (split_msg[0].equals("logged-in")) {
			registerBtn.setVisible(false);
			signIn.setVisible(false);
			enterPasswordField.setVisible(false);
			enterNameField.setVisible(false);
			namePrompt.setVisible(false);
			passwordPrompt.setVisible(false);
			send.setVisible(true);
			msgInput.setVisible(true);
			//input.setVisible(true);
			text.setVisible(true);
			logoutBtn.setVisible(true);
			loginError.setText("");
			loginError.setVisible(false);
		} else if (split_msg[0].equals("name-taken")){
			loginError.setText("That username is taken. Please try again.");
		} else if (split_msg[0].equals("name-not-found")){
			loginError.setText("Username not recognized. Please try again.");
		} else if (split_msg[0].equals("already-online")) {
			loginError.setText("This user is already online. Please try again.");
		} else if (split_msg[0].equals("wrong-password")) {
			loginError.setText("Password is incorrect. Please try again.");
		} else {
			// TODO make this append instead
			final String msg = ((String)message);
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					//input.setText(msg);
				}
			});
			
			
		}
	}
	public Message createMessage() {
		return new Message(currentChatroom, userNum, msgInput.getText());
	}
	private void createTab(Chatroom cr) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Tab tab = new Tab();
				tab.setText(cr.getName());
				TextArea textArea = new TextArea();
				tab.setContent(textArea);
				tabs.put(cr.getChatroomNum(), tab);
				tabPane.getTabs().add(tab);
			}
		});
	}
	private void removeTab(int chatroom) {
		tabPane.getTabs().remove(tabs.get(chatroom));
		tabs.remove(chatroom);
	}
}
