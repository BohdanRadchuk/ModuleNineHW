import com.mashape.unirest.http.HttpResponse;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import objectsStructure.YouTubeMainResponse;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void youtubeSearch(Pane root, Stage stage) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        Button btnSearch = new Button("Search");
        btnSearch.setTranslateX(200);
        btnSearch.setTranslateY(10);
        TextField youTubeChannel = new TextField("UCmSwqv2aPbuOGiuii2TeaLQ");
        youTubeChannel.setTranslateX(10);
        youTubeChannel.setTranslateY(10);

        btnSearch.setOnAction(event -> {
            root.getChildren().clear();
            root.getChildren().addAll(btnSearch, youTubeChannel);
            pool.submit(() -> {
                HttpResponse<YouTubeMainResponse> response = YouTubeApi.youTubeApiWork(youTubeChannel.getText());
                System.out.println("response Code " + response.getStatus());
                YouTubeMainResponse body = response.getBody();
                Platform.runLater(() -> {

                    Text channelName = new Text("Channel name: " + body.items.get(0).snippet.channelTitle);
                    channelName.setTranslateX(10);
                    channelName.setTranslateY(110);

                    Text videoName = new Text("video name: " + '"' + body.items.get(0).snippet.title + '"');
                    videoName.setTranslateX(10);
                    videoName.setTranslateY(160);

                    Text videoPublishDate = new Text("video published at " + body.items.get(0).snippet.publishedAt);
                    videoPublishDate.setTranslateX(videoName.getLayoutBounds().getWidth() + 50);
                    videoPublishDate.setTranslateY(160);
                    WebView webview = new WebView();
                    System.out.println("https://www.youtube.com/watch?v=" + body.items.get(0).contentDetails.upload.videoID);
                    webview.getEngine().load(
                            "https://www.youtube.com/watch?v=" + body.items.get(0).contentDetails.upload.videoID                    );
                    webview.setPrefSize(640, 390);
                    Scene videoScene = new Scene(webview);
                    Stage stage1 = new Stage();
                    stage.setScene(videoScene);
                    stage.show();
                    Media media = new Media("https://www.youtube.com/watch?v=" + body.items.get(0).contentDetails.upload.videoID);
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                    MediaView mediaView = new MediaView(mediaPlayer);


                    root.getChildren().addAll(videoName, channelName, videoPublishDate, mediaView);

                    System.out.println(body.items.get(0).snippet.channelId);
                    System.out.println("published at" + body.items.get(0).snippet.publishedAt);
                    System.out.println("sniped title " + body.items.get(0).snippet.title);
                    System.out.println("channel title " + body.items.get(0).snippet.channelTitle);
                });
            });

        });


        root.getChildren().addAll(btnSearch, youTubeChannel);

    }


    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        /*Pane youtubeRoot = new Pane();
        youtubeRoot.setTranslateY(100);
*/
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Youtube channel info");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setHeight(dimension.height - 50);
        primaryStage.setWidth(dimension.width);
        primaryStage.show();
        //      root.getChildren().addAll(youtubeRoot);
        youtubeSearch(root, primaryStage);

    }


}
