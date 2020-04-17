package dolchlist;

import java.io.File;
import java.util.List;

import dolchlist.config.ButtonConfig;
import dolchlist.config.DolchListConfig;
import dolchlist.config.PropertyLoader;
import dolchlist.config.StageBuilder;
import dolchlist.dto.DolchListElement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private PropertyLoader propertyLoader = new PropertyLoader();
    private DolchListConfig dolchListConfig = new DolchListConfig(propertyLoader);
    private StageBuilder stageBuilder = new StageBuilder();
    private ButtonConfig buttonConfig = new ButtonConfig(stageBuilder, dolchListConfig, propertyLoader);

    private MediaPlayer mediaplayer;
    private MediaPlayer mediaplayer2;

    public static void main(String[] args) {
        logger.info("Starting DolchList2");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        List<DolchListElement> random3Words = dolchListConfig.pick3Words();

        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, propertyLoader.getSizeX(), propertyLoader.getSizeY());
            Pane pane = new Pane();
            root.setCenter(pane);

            stageBuilder.addButton(buttonConfig.createButton(pane, 200.0));
            stageBuilder.addButton(buttonConfig.createButton(pane, 500.0));
            stageBuilder.addButton(buttonConfig.createButton(pane, 800.0));

            stageBuilder.assign1CorrectWord(random3Words);
            stageBuilder.addLabel(pane);

            readInitialInstruction();

            //primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setResizable(true);
            //primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            //primaryStage.setFullScreen(true);
            primaryStage.requestFocus();
            primaryStage.setAlwaysOnTop(false);
            primaryStage.setAlwaysOnTop(true);
            primaryStage.toFront();
            primaryStage.show();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void readInitialInstruction() {
        String fileUrl = propertyLoader.getSoundsFolder() + "/helper/find.mp3";
        File audioFile = new File(fileUrl);
        Media audio = new Media(audioFile.toURI().toString());
        mediaplayer = new MediaPlayer(audio);
        mediaplayer.play();
        mediaplayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaplayer.stop();
                String fileUrl2 = stageBuilder.getCorrectButton().getDolchListElement().getFilePath();
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
