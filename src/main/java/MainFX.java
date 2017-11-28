import com.mashape.unirest.http.HttpResponse;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import objectsStructure.Items;
import objectsStructure.YouTubeMainResponse;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFX extends Application {
    private static final int WINDOW_HEIGHT = 900;
    private static final int WINDOW_WIDTH = 1350;

    private static final int VERTICAL_OFFSET = 50;

    public static void main(String[] args) {
        launch(args);
    }

    public void youtubeSearch(Pane root, Stage stage) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        Pane youPane = new Pane();
        ScrollPane scrollPane = new ScrollPane(youPane);
        scrollPane.setTranslateX(0);
        scrollPane.setTranslateY(VERTICAL_OFFSET);
        scrollPane.setPannable(true);
        scrollPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT - VERTICAL_OFFSET);

        // авто обновление размеров для scrollPane при ресайзе окна
        root.heightProperty().addListener((arg0, arg1, arg2) -> {
            scrollPane.setPrefHeight(arg2.doubleValue() - VERTICAL_OFFSET);
        });
        root.widthProperty().addListener((arg0, arg1, arg2) -> {
            scrollPane.setPrefWidth(arg2.doubleValue());
        });
        youPane.setPrefHeight(180*10);
        //youPane.setTranslateY(150);
        Button btnSearch = new Button("Search");
        btnSearch.setTranslateX(200);
        btnSearch.setTranslateY(10);
        String channelSearch = "UCmSwqv2aPbuOGiuii2TeaLQ";
        channelSearch = "UCIupfj3rki6dfjQqFKbXzMA";
        TextField youTubeChannel = new TextField(channelSearch);
        youTubeChannel.setTranslateX(10);
        youTubeChannel.setTranslateY(10);
        youPane.getChildren().addAll(btnSearch);

        btnSearch.setOnAction(event -> {
           /* root.getChildren().clear();
            root.getChildren().addAll(btnSearch, youTubeChannel);
           */
           pool.submit(() -> {
               //youPane.getChildren().clear();
                HttpResponse<YouTubeMainResponse> response = YouTubeApi.youTubeApiWork(youTubeChannel.getText(), 10);
                System.out.println("response Code " + response.getStatus());
                YouTubeMainResponse body = response.getBody();
               ArrayList <Button> btnPlay = new ArrayList<>();

                for (int i = 0; i <body.items.size(); i++) {
                    btnPlay.add(new Button("View"));
                    Items item = body.items.get(i);
                    int finalI = i;

                    Platform.runLater(() -> {

                        Image image = new Image(item.snippet.thumbnails.medium.url);
                        double translateY = image.getHeight()* finalI;
                        double trnaslateX = image.getWidth() + 50;
                        System.out.println();
                        ImageView imageView = new ImageView(image);
                        imageView.setTranslateX(10);
                        imageView.setTranslateY(translateY);

                        Text channelName = new Text("Channel name: " + item.snippet.channelTitle);
                        channelName.setTranslateX(trnaslateX);
                        channelName.setTranslateY(translateY +40);

                        Text videoName = new Text("video name: " + '"' + item.snippet.title + '"');
                        videoName.setTranslateX(trnaslateX);
                        videoName.setTranslateY(translateY + 80);

                        Text videoPublishDate = new Text("video published at " + item.snippet.publishedAt);
                        videoPublishDate.setTranslateX(trnaslateX + videoName.getLayoutBounds().getWidth() + 50);
                        videoPublishDate.setTranslateY(translateY + 80);


                        btnPlay.get(finalI).setTranslateX(800);
                        btnPlay.get(finalI).setTranslateY(translateY);
                        youPane.getChildren().addAll(btnPlay.get(finalI));
                        btnPlay.get(finalI).setOnAction(event1 -> {
                            WebView webView = myWebView(item); // categories.indexOf(category)

                            youPane.getChildren().addAll(webView);

                        });



                        /*WebView webview = new WebView();
                        System.out.println("https://www.youtube.com/watch?v=" + item.contentDetails.upload.videoId);
                        webview.getEngine().load("https://www.youtube.com/watch?v=" + item.contentDetails.upload.videoId);
                        webview.setPrefSize(640, 390);
                        webview.setTranslateX(400);
                        webview.setTranslateY(200);*/
                   /* Scene videoScene = new Scene(webview);
                    Stage stage1 = new Stage();
                    stage.setScene(videoScene);
                    stage.show();*/
                    /*Media media = new Media("https://www.youtube.com/watch?v=" + body.items.get(0).contentDetails.upload.videoId);
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                    MediaView mediaView = new MediaView(mediaPlayer);

*/
                        youPane.getChildren().addAll(videoName, channelName, videoPublishDate, imageView);

                        System.out.println(item.snippet.channelId);
                        System.out.println("published at" + item.snippet.publishedAt);
                        System.out.println("sniped title " + item.snippet.title);
                        System.out.println("channel title " + item.snippet.channelTitle);
                    });
                }
            });

        });


        root.getChildren().addAll(btnSearch, youTubeChannel, youPane, scrollPane);

    }

    public WebView myWebView (Items item){
        WebView webview = new WebView();
        System.out.println("https://www.youtube.com/watch?v=" + item.contentDetails.upload.videoId);
        webview.getEngine().load("https://www.youtube.com/watch?v=" + item.contentDetails.upload.videoId);
        webview.setPrefSize(640, 390);
        webview.setTranslateX(400);
        webview.setTranslateY(50);
        return webview;
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
            primaryStage.setHeight(dimension.height - 100);
        primaryStage.setWidth(dimension.width);
        primaryStage.show();
        //      root.getChildren().addAll(youtubeRoot);
        youtubeSearch(root, primaryStage);

    }


}
