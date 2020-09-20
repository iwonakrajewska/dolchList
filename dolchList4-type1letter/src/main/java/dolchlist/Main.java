package dolchlist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dolchlist.config.DolchListConfig;
import dolchlist.config.PropertyLoader;
import dolchlist.config.StageBuilder;
import dolchlist.dto.Word;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	private PropertyLoader propertyLoader = new PropertyLoader();
	private DolchListConfig dolchListConfig = new DolchListConfig(propertyLoader);
	private StageBuilder stageBuilder = new StageBuilder();
	private Word wordToType = new Word();

	public static void main(String[] args) {
		LOGGER.info("Starting DolchList4");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		wordToType.assignWord(dolchListConfig);

		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, propertyLoader.getSizeX(), propertyLoader.getSizeY());
			Pane pane = new Pane();
			root.setCenter(pane);

			stageBuilder.paintStageObjects(pane, wordToType);

			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setResizable(true);
			// primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			// primaryStage.setFullScreen(true);
			primaryStage.requestFocus();
			primaryStage.setAlwaysOnTop(false);
			primaryStage.setAlwaysOnTop(true);
			primaryStage.toFront();
			primaryStage.show();

			stageBuilder.readInitialInstruction(propertyLoader, wordToType);

			scene.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					stageBuilder.readInitialInstruction(propertyLoader, wordToType);
				}
			});

			handleSideBehaviour(primaryStage);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void handleSideBehaviour(Stage primaryStage) {
		primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ESCAPE)) {
					if (wordToType.isExitEnabled()) {
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
				wordToType.getFieldWithFocus().requestFocus();
			}
		});

	}

}
