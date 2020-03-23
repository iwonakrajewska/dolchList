package sample;

import java.io.File;

import config.DolchListConfig;
import config.DolchListElement;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		
		DolchListConfig dolchListConfig = new DolchListConfig();

		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, dolchListConfig.getSizeX(), dolchListConfig.getSizeY());

			Pane pane = new Pane();
			Text text1 = new Text("No mp3 file found in:");
			Text text2 = new Text(dolchListConfig.getSoundsFolder());
			text1.setX(200);
			text1.setY(200);
			text1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 100));
			text2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 100));
			text2.setX(200);
			text2.setY(500);
			pane.getChildren().add(text1);
			pane.getChildren().add(text2);

			Task<Void> task = new Task<Void>() {
				String a = "";
				String b = "";

				@Override
				public Void call() throws Exception {
					int i = 0;
					String fileUrl;
					File audioFile;
					Media audio;
					MediaPlayer audioPlayer = null;
					DolchListElement w = dolchListConfig.pickRandomWord();

					while (true) {
						if (i == 0) {
							a = "";
							b = "";
							w = dolchListConfig.pickRandomWord();
							fileUrl = w.getFilePath();
							audioFile = new File(fileUrl);
							audio = new Media(audioFile.toURI().toString());
							audioPlayer = new MediaPlayer(audio);

							a = w.getWord();
							audioPlayer.play();
						}

						if (i == 1) {
							audioPlayer.stop();
							b = w.getWord();
							audioPlayer.play();
						}

						if (i == 2) {
							audioPlayer.stop();
							i = 0;
							Platform.exit();
							System.exit(0);
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
			}
			root.setCenter(pane);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setResizable(true);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
