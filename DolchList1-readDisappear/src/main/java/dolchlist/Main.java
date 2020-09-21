package dolchlist;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dolchlist.config.DolchListConfig;
import dolchlist.config.DolchListElement;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		LOGGER.info("Starting DolchList1");

		DolchListConfig dolchListConfig = new DolchListConfig();

		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, dolchListConfig.getSizeX(), dolchListConfig.getSizeY());

			Pane pane = new Pane();
			Text text1 = new Text("");
			Text text2 = new Text("");
			text1.setX(300);
			text1.setY(400);
			text1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 100));
			text2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 100));
			text2.setX(300);
			text2.setY(700);
			pane.getChildren().add(text1);
			pane.getChildren().add(text2);

			Task<Void> task = new Task<Void>() {
				String a = "";
				String b = "";

				@Override
				public Void call() throws Exception {
					int i = 0;
					int currentIteration = 0;
					String fileUrl;
					File audioFile;
					Media audio;
					MediaPlayer audioPlayer = null;
					DolchListElement w = dolchListConfig.pickRandomWord();

					while (true) {
						if (i == 0) {
							currentIteration++;
							a = "";
							b = "";
							w = dolchListConfig.pickRandomWord();
							fileUrl = w.getFilePath();
							audioFile = new File(fileUrl);
							audio = new Media(audioFile.toURI().toString());
							audioPlayer = new MediaPlayer(audio);
							LOGGER.info("Playing: {}", w.getWord());
							a = w.getWord();
							audioPlayer.play();
							dolchListConfig.setExitEnabled(true);
						}

						if (i == 1) {
							audioPlayer.stop();
							b = w.getWord();
							audioPlayer.play();
						}

						if (i == 2) {
							audioPlayer.stop();
							i = -1;
							if (currentIteration >= dolchListConfig.getIterations()) {
								LOGGER.info("Iterations reached limit. Finish.");
								Platform.exit();
								System.exit(0);
							}
						}

						Platform.runLater(() -> {
							text1.setText(a);
							text2.setText(b);
						});

						i++;
						Thread.sleep(1000);
					}
				}
			};
			Thread th = new Thread(task);
			// th.setDaemon(true);
			if (dolchListConfig.pickRandomWord() != null) {
				th.start();
			} else {
				LOGGER.warn("No sound files found in {}", dolchListConfig.getSoundsFolder());
				text1.setText("No mp3 file found in:");
				text2.setText(dolchListConfig.getSoundsFolder());
			}
			root.setCenter(pane);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setResizable(true);
			primaryStage.setScene(scene);
			//primaryStage.setFullScreen(true);
			primaryStage.requestFocus();
			primaryStage.setAlwaysOnTop(false);
			primaryStage.setAlwaysOnTop(true);
			primaryStage.toFront();
			primaryStage.show();

			 handleSideBehaviour(primaryStage, dolchListConfig);
            
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	
	  
    private void handleSideBehaviour(Stage primaryStage, DolchListConfig dolchListConfig) {
		primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ESCAPE)) {
					if (dolchListConfig.isExitEnabled()) {
						LOGGER.info("Consuming Escape on stage, exiting");
						Platform.exit();
						System.exit(0);
					}
				}
			}
		});

		primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown) {
				LOGGER.info("focus property changed");
				primaryStage.setAlwaysOnTop(false);
				primaryStage.setAlwaysOnTop(true);
				primaryStage.toFront();
			}
		});

	}


}
