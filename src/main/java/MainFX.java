import com.mashape.unirest.http.HttpResponse;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import objectsStructure.YouTubeMainResponce;

import java.awt.*;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void youtubeSearch(Pane root){
        Pane youtubeRoot = new Pane();
        youtubeRoot.setTranslateY(100);
        Button btnSearch = new Button("Search");
        YouTubeApi youTubeApi = new YouTubeApi();

    }


    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

    }



}
