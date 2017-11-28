import com.mashape.unirest.http.HttpResponse;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

    private static final int OFFSET = 50;

    public static void main(String[] args) {
        launch(args);
    }

    public void youtubeSearch(Pane root) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        int maxResults = 25;                                                //кол-во запросов
        Pane youPane = new Pane();
        youPane.setPrefWidth(dimension.width - OFFSET);
        youPane.setPrefHeight(180 * maxResults + OFFSET);
        ScrollPane scrollPane = new ScrollPane(youPane);
        scrollPane.setTranslateX(0);
        scrollPane.setTranslateY(OFFSET);
        scrollPane.setPannable(true);
        scrollPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT - OFFSET);

        // авто обновление размеров для scrollPane при ресайзе окна
        root.heightProperty().addListener((arg0, arg1, arg2) -> {
            scrollPane.setPrefHeight(arg2.doubleValue() - OFFSET);
        });
        root.widthProperty().addListener((arg0, arg1, arg2) -> {
            scrollPane.setPrefWidth(arg2.doubleValue());
        });
        Button btnSearch = new Button("Search");
        btnSearch.setTranslateX(200);
        btnSearch.setTranslateY(10);
        String channelSearch = "UCilV44nrGb-2FKvogc9bI9A";                              //тут указываем дефолтное значение в поле для поиска каналов

        TextField youTubeChannel = new TextField(channelSearch);
        youTubeChannel.setTranslateX(10);
        youTubeChannel.setTranslateY(10);
        youPane.getChildren().addAll(btnSearch);

        btnSearch.setOnAction(event -> {

            youPane.getChildren().clear();
            pool.submit(() -> {

                HttpResponse<YouTubeMainResponse> response = YouTubeApi.youTubeApiWork(youTubeChannel.getText(), maxResults);
                System.out.println("response Code " + response.getStatus());
                YouTubeMainResponse body = response.getBody();
                ArrayList<Button> btnPlay = new ArrayList<>();

                for (int i = 0; i < body.items.size(); i++) {
                    btnPlay.add(new Button("View"));
                    Items item = body.items.get(i);
                    int finalI = i;

                    Platform.runLater(() -> {

                        Image image = new Image(item.snippet.thumbnails.medium.url);
                        double translateY = image.getHeight() * finalI;                                 //сдвигаем на высоту картинки
                        double translateX = image.getWidth() + OFFSET;
                        System.out.println();
                        ImageView imageView = new ImageView(image);
                        imageView.setTranslateX(10);
                        imageView.setTranslateY(translateY);

                        Text channelName = new Text("Channel name: " + item.snippet.channelTitle);
                        channelName.setTranslateX(translateX);
                        channelName.setTranslateY(translateY + OFFSET);

                        Text videoName = new Text("video name: " + '"' + item.snippet.title + '"');
                        videoName.setTranslateX(translateX);
                        videoName.setTranslateY(translateY + 80);

                        Text videoPublishDate = new Text("video published at " + item.snippet.publishedAt);
                        videoPublishDate.setTranslateX(translateX + videoName.getLayoutBounds().getWidth() + OFFSET);
                        videoPublishDate.setTranslateY(translateY + 80);

                        btnPlay.get(finalI).setTranslateX(translateX + channelName.getLayoutBounds().getWidth() + 10);
                        btnPlay.get(finalI).setTranslateY(translateY + 35);
                        youPane.getChildren().addAll(btnPlay.get(finalI));

                        btnPlay.get(finalI).setOnAction(event1 -> {
                            int inda = btnPlay.indexOf(btnPlay.get(finalI));
                            Items btnitem = body.items.get(inda);

                            //checking for upload activity type (можно было сделать воспроизведение и из другого типа активности, но я так понял что их может быть много разных,
                            //  а играть видео по умолчанию должны с 1 типа - загрузки файла на ютуб, как пример UCmSwqv2aPbuOGiuii2TeaLQ канал - там каждая вторая активность это
                            // "bulletin", есть каналы в которых по три активности на 1 видео. для этого и выдаётся ошибка воспроизвидения)
                            if (!btnitem.snippet.type.equals("upload")) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);

                                alert.setTitle("ERROR");
                                alert.setHeaderText("Not a video upload activity type");
                                alert.showAndWait();
                            } else myWebView(btnitem, youPane);
                        });

                        youPane.getChildren().addAll(videoName, channelName, videoPublishDate, imageView);

                        /*System.out.println(item.snippet.channelId);
                        System.out.println("published at" + item.snippet.publishedAt);
                        System.out.println("sniped title " + item.snippet.title);
                        System.out.println("channel title " + item.snippet.channelTitle);*/
                    });
                }
            });
        });
        root.getChildren().addAll(btnSearch, youTubeChannel, youPane, scrollPane);
    }

    public void myWebView(Items item, Pane youPane) {                               //воспроизведение видео с помощью WebView
        String youWatch = "https://www.youtube.com/watch?v=";
        WebView webview = new WebView();
        webview.getEngine().load(youWatch + item.contentDetails.upload.videoId);
        webview.setPrefSize(640, 390);
        webview.setTranslateX(750);
        webview.setTranslateY(100);
        youPane.getChildren().addAll(webview);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Youtube channel info");
        primaryStage.setHeight(dimension.height - 100);
        primaryStage.setWidth(dimension.width);
        primaryStage.show();
        youtubeSearch(root);

    }


}
