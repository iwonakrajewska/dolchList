package dolchlist.dto;

import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;

import java.awt.Font;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InputTextElement {

	// TextField t = new TextField("xx");
	// TextInputDialog dialog = new TextInputDialog("walter");

	public InputTextElement() {
	}
//		t.setText("" + word.getWordToType().getWord().charAt(index));
//		
//		
//		
//		
//		 final Button button1 = new Button();
//	        button1.setLayoutX(200);
//	        button1.setLayoutY(layoutY);
//	        button1.setText("word1");
//	        button1.setMinWidth(1000);
//	        //button1.setMaxWidth(500);
//	        button1.setPrefWidth(1000);
//	        button1.setMinHeight(200);
//	        button1.setMaxHeight(200);
//	        button1.setPrefHeight(200);
//	        button1.setStyle("-fx-font-size: 10em;  -fx-font-family: Verdana; ");
//	        pane.getChildren().add(button1);
//
//	}

	public void printInputText(Word word, int index, Pane pane, double layoutX, double layoutY) {
		TextField textField = new TextField();
		textField.setText("" + word.getWordToType().getWord().charAt(index));
		textField.setLayoutX(layoutX);
		textField.setLayoutY(layoutY);

		textField.setMinSize(200, 200);
		textField.setMaxSize(200, 200);
		textField.setStyle("-fx-font-size: 8em;  -fx-font-family: Verdana; -fx-padding: 0 0 0 60;   ");
		
	 
	   

		if (index == word.getLetterIndex()) {
			textField.setEditable(true);
			textField.setText("");
			textField.requestFocus();
		
		} else {
			textField.setEditable(false);
		}

		setTextLimit(textField, 1);

		pane.getChildren().add(textField);
	}

	private void setTextLimit(TextField textField, int length) {
		textField.setOnKeyTyped(event -> {
			String string = textField.getText();

			if (string.length() >= length) {
				textField.setText(string.substring(0, length-1));
				textField.positionCaret(length-1);
			}
			
			textField.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px; -fx-font-size: 8em;  -fx-font-family: Verdana; -fx-padding: 0 0 0 60; ");

			//textField.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px; -fx-font-size: 8em;  -fx-font-family: Verdana; -fx-padding: 0 0 0 60;  ");
		});
	}

}
