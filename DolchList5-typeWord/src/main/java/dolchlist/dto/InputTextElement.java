package dolchlist.dto;

import java.io.File;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputTextElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputTextElement.class);

    private Word selectedWord;

    private MediaPlayer mediaplayer;

    public InputTextElement(Word word) {
        this.selectedWord = word;
    }

    public void printInputText(Pane pane, double layoutX, double layoutY) {
        TextField textField = new TextField();
        textField.setText("");
        textField.setLayoutX(layoutX);
        textField.setLayoutY(layoutY);

        textField.setMinSize(200 * selectedWord.getWordToType().getWord().length(), 200);
        textField.setMaxSize(200 * selectedWord.getWordToType().getWord().length(), 200);
        textField.setStyle("-fx-font-size: 10em;  -fx-font-family: Verdana; -fx-padding: 0 0 0 60; ");
        textField.setEditable(true);
        textField.setFocusTraversable(true);

        selectedWord.setFieldWithFocus(textField);
        textField.requestFocus();

        checkFieldValueAction(textField);

        pane.getChildren().add(textField);
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


        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                try {
                    // force correct length by resetting to old value if longer than maxLength
                    if (newValue.length() > selectedWord.getWordToType().getWord().length()) {
                        textField.setText(oldValue);
                    }
                } catch (Exception e) {
                    textField.setText(oldValue);
                }
            }
        });

        textField.setOnKeyReleased(event -> {
            String inputValue = textField.getText();
            LOGGER.info("Checking for inputValue {}", inputValue);

            if (selectedWord.getWordToType().getWord().length() > inputValue.length()) {
                return;
            }

            if (!selectedWord.getWordToType().getWord().equalsIgnoreCase(inputValue)) {
                textField.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px; -fx-font-size: 10em;  -fx-font-family: Verdana; -fx-padding: 0 0 0 60; ");
                return;
            }

            textField.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px; -fx-font-size: 10em;  -fx-font-family: Verdana; -fx-padding: 0 0 0 60; ");
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
    }

}
