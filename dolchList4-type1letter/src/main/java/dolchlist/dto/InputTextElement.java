package dolchlist.dto;

import java.io.File;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class InputTextElement {

	private static final Logger LOGGER = LoggerFactory.getLogger(InputTextElement.class);

	private Word selectedWord;

	private MediaPlayer mediaplayer;

	public InputTextElement(Word word) {
		this.selectedWord = word;
	}

	public void printInputText(Pane pane, int index, double layoutX, double layoutY) {
		TextField textField = new TextField();
		textField.setText("" + selectedWord.getWordToType().getWord().charAt(index));
		textField.setLayoutX(layoutX);
		textField.setLayoutY(layoutY);

		textField.setMinSize(200, 200);
		textField.setMaxSize(200, 200);
		textField.setStyle("-fx-font-size: 8em;  -fx-font-family: Verdana; -fx-padding: 0 0 0 60; ");
		textField.setEditable(false);
		textField.setFocusTraversable(false);

		if (index == selectedWord.getLetterIndex()) {
			textField.setEditable(true);
			textField.setFocusTraversable(true);
			textField.requestFocus();
			textField.setText("");
			textField.setStyle("-fx-border-color: #0099cc; -fx-border-width: 5px; -fx-font-size: 8em;  -fx-font-family: Verdana; -fx-padding: 0 0 0 60; ");

			setFieldLengthAction(textField, 1);
			checkFieldValueAction(textField);
			selectedWord.setFieldWithFocus(textField);

		}
		pane.getChildren().add(textField);
	}

	private void setFieldLengthAction(TextField textField, int length) {
		textField.setOnKeyTyped(event -> {
			String inputValue = textField.getText();
			if (inputValue.length() < 1) {
				return;
			}
			if (inputValue.length() >= length) {
				textField.setText(inputValue.substring(0, length - 1));
				textField.positionCaret(length - 1);
			}
		});
	}

	private void checkFieldValueAction(TextField textField) {

		Consumer<String> triggerExit = text -> {
			LOGGER.info("Triggering exit after {}", text);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				LOGGER.error("Unhandled exception caught", e1);
			}
			Platform.exit();
			System.exit(0);
		};

		textField.setOnKeyReleased(event -> {
			String inputValue = textField.getText();
			LOGGER.info("Checking for inputValue {}", inputValue);
			if (!selectedWord.getLetterText().equalsIgnoreCase(inputValue)) {
				textField.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px; -fx-font-size: 8em;  -fx-font-family: Verdana; -fx-padding: 0 0 0 60; ");
				return;
			}

			textField.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px; -fx-font-size: 8em;  -fx-font-family: Verdana; -fx-padding: 0 0 0 60; ");
			selectedWord.setExitEnabled(true);
			
			String fileUrl = selectedWord.getWordToType().getSoundFilePath();
			File audioFile = new File(fileUrl);
			Media audio = new Media(audioFile.toURI().toString());
			mediaplayer = new MediaPlayer(audio);
			mediaplayer.play();
			mediaplayer.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() {
					mediaplayer.stop();

					triggerExit.accept(selectedWord.getWordToType().getWord());
				}
			});
		});
		
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown) {
				LOGGER.info("textField focus property changed");
				selectedWord.getFieldWithFocus().requestFocus();
			}
		});
	}
	
}
