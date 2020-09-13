package dolchlist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dolchlist.config.DolchListConfig;
import dolchlist.config.PropertyLoader;
import dolchlist.config.StageBuilder;
import dolchlist.dto.Word;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
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
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
