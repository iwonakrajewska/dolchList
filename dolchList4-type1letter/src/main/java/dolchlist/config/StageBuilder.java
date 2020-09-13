/*
 * Copyright (c) 2020 Mastercard. All rights reserved.
 */

package dolchlist.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dolchlist.Main;
import dolchlist.dto.ButtonElement;
import dolchlist.dto.DolchListElement;
import dolchlist.dto.InputTextElement;
import dolchlist.dto.Word;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StageBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(StageBuilder.class);
	private static final double LAYOUT_X = 200.0;

//	Map<Button, ButtonElement> buttonMap = new HashMap();
//	private Random randomGenerator = new Random();
//	private ButtonElement correctButton;
//
	private MediaPlayer mediaplayer;
	private MediaPlayer mediaplayer2;

	public void paintStageObjects(Pane pane, Word word) {
		printInstruction(pane, 50.0);
		printWordToType(pane, LAYOUT_X + 200, 50.0, word);
		printLetterInputBoxes(pane, word, 300.0);
		printPicture(pane, 600.0, word);
	}

	private void printLetterInputBoxes(Pane pane, Word word,  double layoutY) {
		int wordLength = word.getWordToType().getWord().length();
		
		InputTextElement input = new InputTextElement();
		for (int i = 0; i < wordLength; i++) {
			double layoutX = 200*i + LAYOUT_X;
			input.printInputText(word, i, pane, layoutX, layoutY);
		}
	}

//	public void addButton(Button button) {
//		buttonMap.put(button, new ButtonElement(button));
//	}
//
//	public void assign1CorrectWord(List<DolchListElement> element3words) {
//		int indexForCorrect = randomGenerator.nextInt(buttonMap.size());
//		int i = 0;
//		for (Button b : buttonMap.keySet()) {
//			DolchListElement word = element3words.get(i);
//			ButtonElement buttonElement = buttonMap.get(b);
//			buttonElement.getButton().setText(word.getWord());
//			buttonElement.setDolchListElement(word);
//
//			if (i == indexForCorrect) {
//				buttonElement.setCorrect(true);
//				correctButton = buttonElement;
//			}
//			i++;
//		}
//	}
//
//	public ButtonElement getByButton(Button button) {
//		return buttonMap.get(button);
//	}
//
//	public ButtonElement getCorrectButton() {
//		return correctButton;
//	}

	private void printInstruction(Pane pane, double layoutY) {
		Label label1 = new Label("Type:  ");
		label1.setLayoutX(LAYOUT_X);
		label1.setLayoutY(layoutY);
		label1.setStyle("-fx-font-size: 5em;  -fx-font-family: Verdana; ");
		pane.getChildren().add(label1);
	}

	private void printWordToType(Pane pane, double layoutX, double layoutY, Word worde) {
		Label label2 = new Label(worde.getWordToType().getWord());
		label2.setLayoutX(layoutX);
		label2.setLayoutY(layoutY);
		label2.setStyle("-fx-font-size: 10em;  -fx-font-family: Verdana; ");
		pane.getChildren().add(label2);
	}

	private void printPicture(Pane pane, double layoutY, Word word) {
		try {
			Image image = new Image(new FileInputStream(word.getWordToType().getPictureFileParh()));
			ImageView imageView = new ImageView(image);
			imageView.setX(LAYOUT_X);
			imageView.setY(layoutY);
			// setting the fit height and width of the image view
			imageView.setFitHeight(455);
			imageView.setFitWidth(500);
			// Setting the preserve ratio of the image view
			imageView.setPreserveRatio(true);
			pane.getChildren().add(imageView);
		} catch (FileNotFoundException e) {
			LOGGER.error("Picture file doesnt exist: {}", word.getWordToType().getPictureFileParh());
		}
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
