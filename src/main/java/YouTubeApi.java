import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import objectsStructure.YouTubeMainResponce;

public class YouTubeApi {
    private final static String YOUTUBE_ADRESS = "https://www.googleapis.com/youtube/v3/activities";
    private final static String MY_API_KEY  = "AIzaSyCl0nW-_pC68yOaRAvktYuByEKxYITPYNk";
    public static HttpResponse<YouTubeMainResponce> youTubeApiWork(String channelID, int maxResults){
                    Unirest.get(YOUTUBE_ADRESS)
                            .queryString("channelId" , channelID)
                    .queryString("maxResults", maxResults)
                    .queryString();
        return null;

    }


}
