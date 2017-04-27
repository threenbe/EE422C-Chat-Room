package assignment7;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class ClientTab {
	TextArea textArea = new TextArea();
	Button tab = new Button();
	// constructor
	public ClientTab(Pane pane, int num) {
		textArea.setPrefRowCount(10);
		textArea.setPrefColumnCount(30);
		textArea.setWrapText(true);
		textArea.setVisible(false);
		pane.getChildren().add(textArea);
		
		//button.setText()
		tab.setLayoutX(50*num);
		tab.setLayoutY(50);
		pane.getChildren().add(tab);
	}
	
	public void addMessage(String msg) {
		textArea.appendText("\n" + msg);
	}
	public void showTab() {
		textArea.setVisible(true);
	}
	public void hideTab() {
		textArea.setVisible(false);
	}
}
