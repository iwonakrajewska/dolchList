/*
 * Copyright (c) 2020 Mastercard. All rights reserved.
 */

package dolchlist.config;

import java.io.File;
import java.util.function.Consumer;

import dolchlist.dto.ButtonElement;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonConfig {

    private static final Logger logger = LoggerFactory.getLogger(ButtonConfig.class);
    private final PropertyLoader propertyLoader;
    private final DolchListConfig dolchListConfig;

    private StageBuilder stageBuilder;

    private MediaPlayer mediaplayer;

    public ButtonConfig(StageBuilder stageBuilder, DolchListConfig dolchListConfig, PropertyLoader propertyLoader) {
        this.stageBuilder = stageBuilder;
        this.dolchListConfig = dolchListConfig;
        this.propertyLoader = propertyLoader;
    }


    public Button createButton(Pane pane, double layoutY) {
        final Button button1 = new Button();
        button1.setLayoutX(200);
        button1.setLayoutY(layoutY);
        button1.setText("word1");
        button1.setMinWidth(1000);
        //button1.setMaxWidth(500);
        button1.setPrefWidth(1000);
        button1.setMinHeight(200);
        button1.setMaxHeight(200);
        button1.setPrefHeight(200);
        button1.setStyle("-fx-font-size: 10em;  -fx-font-family: Verdana; ");
        pane.getChildren().add(button1);

        Consumer<String> triggerExit = text -> {
            logger.info("Triggering exit after " + text);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                logger.error("Unhandled exception caught", e1);
            }
            Platform.exit();
            System.exit(0);
        };

        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                ButtonElement buttonElement=null ; //= stageBuilder.getByButton(button1);
                logger.info("Button clicked: {}", buttonElement.getDolchListElement().getWord());

                if (buttonElement.isCorrect()) {

                    button1.setStyle("-fx-border-color: #00ff00; -fx-border-width: 5px; -fx-font-size: 10em;  -fx-font-family: Verdana;  ");

                    String fileUrl = buttonElement.getDolchListElement().getSoundFilePath();
                    File audioFile = new File(fileUrl);
                    Media audio = new Media(audioFile.toURI().toString());
                    mediaplayer = new MediaPlayer(audio);
                    mediaplayer.play();
                    mediaplayer.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            mediaplayer.stop();
                            String fileUrl2 = propertyLoader.getFilesFolder() + "/../helper/correct.mp3";
                            File audioFile2 = new File(fileUrl2);
                            Media audio2 = new Media(audioFile2.toURI().toString());
                            mediaplayer = new MediaPlayer(audio2);
                            mediaplayer.setAutoPlay(false);
                            mediaplayer.play();

                            triggerExit.accept(buttonElement.getDolchListElement().getWord());
                        }
                    });

                } else {

                    button1.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5px; -fx-font-size: 10em;  -fx-font-family: Verdana;  ");

                    String fileUrl = buttonElement.getDolchListElement().getSoundFilePath();
                    File audioFile = new File(fileUrl);
                    Media audio = new Media(audioFile.toURI().toString());
                    AudioClip ac = new AudioClip(audio.getSource());
                    ac.play();

                }
            }
        };

        // when button is pressed
        button1.setOnAction(event);

        return button1;
    }
}
