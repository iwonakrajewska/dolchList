
package dolchlist.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import dolchlist.dto.InputTextElement;
import dolchlist.dto.Word;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StageBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(StageBuilder.class);
    private static final double LAYOUT_X = 200.0;

    private final PropertyLoader propertyLoader;
    private MediaPlayer mediaplayer;
    private MediaPlayer mediaplayer2;

    public StageBuilder(PropertyLoader propertyLoader) {
        this.propertyLoader = propertyLoader;
    }

    public void paintStageObjects(Pane pane, Word word) {
        printInstruction(pane, 50.0);
        printWordToType(pane, LAYOUT_X + 300, 50.0, word);
        printLetterInputBoxes(pane, word, 300.0);
    }

    private void printLetterInputBoxes(Pane pane, Word word, double layoutY) {
        InputTextElement input = new InputTextElement(word);
        double layoutX = LAYOUT_X + 240;
        input.printInputText(pane, layoutX, layoutY);
    }

    private void printInstruction(Pane pane, double layoutY) {
        Label label1 = new Label("Type:    ");
        label1.setLayoutX(LAYOUT_X);
        label1.setLayoutY(layoutY);
        label1.setStyle("-fx-font-size: 5em;  -fx-font-family: Verdana; ");
        pane.getChildren().add(label1);
    }

    private void printWordToType(Pane pane, double layoutX, double layoutY, Word worde) {
        if (!propertyLoader.isShowHint()) {
            return;
        }
        Label label2 = new Label(worde.getWordToType().getWord());
        label2.setLayoutX(layoutX);
        label2.setLayoutY(layoutY);
        label2.setStyle("-fx-font-size: 10em;  -fx-font-family: Verdana; ");
        pane.getChildren().add(label2);
    }


    public void readInitialInstruction(PropertyLoader propertyLoader, Word wordToType) {
        String fileUrl = propertyLoader.getFilesFolder() + "/../helper/type.mp3";
        File audioFile = new File(fileUrl);
        Media audio = new Media(audioFile.toURI().toString());
        mediaplayer = new MediaPlayer(audio);
        mediaplayer.play();
        mediaplayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaplayer.stop();
                String fileUrl2 = wordToType.getWordToType().getSoundFilePath();
                File audioFile2 = new File(fileUrl2);
                Media audio2 = new Media(audioFile2.toURI().toString());
                mediaplayer2 = new MediaPlayer(audio2);
                mediaplayer2.setAutoPlay(false);
                mediaplayer2.play();
                mediaplayer2.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        mediaplayer2.stop();
                    }
                });

            }
        });
    }
}
